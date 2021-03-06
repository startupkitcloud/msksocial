package org.startupkit.social.spider;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.startupkit.core.address.AddressInfo;
import org.startupkit.core.dao.SearchBuilder;
import org.startupkit.core.exception.BusinessException;
import org.startupkit.core.status.SimpleStatusEnum;
import org.startupkit.core.utils.BusinessUtils;
import org.startupkit.social.post.*;
import org.startupkit.user.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SpiderServiceImpl implements SpiderService {


    @EJB
    private PostService postService;


    @EJB
    private UserService userService;


    @New
    @Inject
    private SpiderDAO spiderDAO;

    @New
    @Inject
    private PostDAO postDAO;


    @Override
    public void save(Spider spider) throws Exception {

        if(spider.getId() == null){
            spider.setStatus(SimpleStatusEnum.ACTIVE);
            spider.setCreationDate(new Date());
        }

        new BusinessUtils<>(spiderDAO).basicSave(spider);
    }

    @Override
    public List<Spider> listAll() throws Exception{
        return spiderDAO.listAll();
    }

    @Override
    public Spider load(String id) throws Exception {

        Spider spider = null;

        spider = spiderDAO.retrieve(new Spider(id));

        if (spider == null){
            throw new BusinessException("spider_not_found");
        }
        return spider;
    }

    @Override
    public void changeStatus(String id) throws Exception {

        Spider spider = spiderDAO.retrieve(new Spider(id));

        if (spider == null){
            throw new BusinessException("spider_not_found");
        }

        if(spider.getStatus().equals(SimpleStatusEnum.ACTIVE)){
            spider.setStatus(SimpleStatusEnum.BLOCKED);
        }else{
            spider.setStatus(SimpleStatusEnum.ACTIVE);
        }

        spiderDAO.update(spider);
    }


    @Override
    public void goSpider() throws Exception {

        //carrega todos os sites ativos
        List<Spider> listSpiders = listByStatus(SimpleStatusEnum.ACTIVE);

        //faz a leitura
        if(listSpiders != null){
            for (Spider spider : listSpiders){

                //busca as noticias
                searchNews(spider);

                //adiciona os posts
            }
        }

    }


    private List<String> searchNews(Spider spider) throws Exception{

        List<String> list = new ArrayList<>();

        List<Post> listAdded = new ArrayList<>();

        Document doc = Jsoup.connect(spider.getUrl()).get();

        Elements els = doc.select("a[href]");

        if(els != null){

            String patternStr = spider.getUrlPatterns().stream()
                    .collect(Collectors.joining(")|(?=.*", "(?=.*", ")"));

            List<String> itens = els.stream()
                    .filter(element -> {
                        Pattern pattern = Pattern.compile(patternStr);
                        return pattern.matcher(element.attr("href")).find();
                    })
                    .map(p -> p.attr("href"))
                    .collect(Collectors.toList());

            if(itens != null){
                for(String url : itens){

                    Post postAdded = listAdded.stream()
                            .filter(p -> p.getInfoUrl().getUrl().equals(url))
                            .findFirst()
                            .orElse(null);

                    if(postAdded == null){

                        // verifica se na tabela de posts já não existe um post com a url
                        SearchBuilder sb = postDAO.createBuilder();
                        sb.appendParamQuery("infoUrl.url", url);
                        List<Post> listPosts = postDAO.search(sb.build());

                        if (listPosts == null || listPosts.size() == 0){

                            //check se ja nao foi processado
                            InfoUrl infoUrl = analyseUrl(spider, url);

                            if(infoUrl != null){
                                //cria o post
                                Post post = createPost(infoUrl, spider);

                                if(post != null){
                                    listAdded.add(post);
                                }
                            }
                        }
                    }
                }
            }
        }

        return list;
    }



    private InfoUrl analyseUrl(Spider spider, String url) {

        InfoUrl infoUrl = null;

        try {

            infoUrl = new InfoUrl();

            if(!url.contains("http")){
                url = spider.getUrlBase() + url;
            }

            Document doc = Jsoup.connect(url).get();

            if(CollectionUtils.isNotEmpty(spider.getTags())){

                String pageDoc = doc.toString().toUpperCase();
                infoUrl.setListTags(new ArrayList<>());

                boolean found = false;
                for(String tag : spider.getTags()){
                    if(pageDoc.toUpperCase().contains(tag.toUpperCase())){
                       found = true;
                       infoUrl.getListTags().add(tag);
                    }
                }

                if(!found){
                    return null;
                }
            }

            Element elTitle = doc.select("meta[name=title]").first();

            if(elTitle != null){
                infoUrl.setTitle(elTitle.attr("content"));
            }
            else{
                infoUrl.setTitle(doc.select("title").first().html());
            }


            Element elFoto = doc.select("meta[property=og:image]").first();
            if(elFoto != null){
                infoUrl.setUrlPhoto(elFoto.attr("content"));
            }

            infoUrl.setUrl(url);
            infoUrl.setSiteName(spider.getName());
        }
        catch (Exception e){
            //nada faz
        }

        return infoUrl;
    }


    private Post createPost(InfoUrl infoUrl, Spider spider) throws Exception{

        List<Post> postsDB = postService.searchByNewsUrl(infoUrl.getUrl());
        if(postsDB == null || postsDB.size() == 0){

            //every post must have an address, in this case a fixed one on Curitiba
            AddressInfo addressInfo = new AddressInfo();
            addressInfo.setLatitude(-25.4343079);
            addressInfo.setLongitude(-49.2594428);
            addressInfo.setStreet("Avenida Sete de Setembro");
            addressInfo.setNumber("1865");
            addressInfo.setDistrict("Centro");
            addressInfo.setCity("Curitiba");
            addressInfo.setState("PR");
            addressInfo.setZipCode("80060070");
            addressInfo.setAddress("Av. Sete de Setembro, 1865 - Centro, Curitiba - PR, 80060-070");

            Post post = new Post();
            if(spider.getIdUserPostCreator() != null && userService.generateCard(spider.getIdUserPostCreator()) != null){
                post.setUserCreator(userService.generateCard(spider.getIdUserPostCreator()));
            }

            post.setStatus(PostStatusEnum.PENDING);
            post.setCreationDate(new Date());
            post.setDesc(infoUrl.getDesc());
            post.setInfoUrl(infoUrl);
            post.setType(PostTypeEnum.NEWS);
            post.setAddress(addressInfo);
            post.setListTags(infoUrl.getListTags());

            postService.save(post, false);

            return post;
        }

        return null;
    }



    @Override
    public List<Spider> listByStatus(SimpleStatusEnum status) throws Exception {
        return spiderDAO.search(new SearchBuilder()
                .appendParamQuery("status", SimpleStatusEnum.ACTIVE)
                .build());
    }

}
