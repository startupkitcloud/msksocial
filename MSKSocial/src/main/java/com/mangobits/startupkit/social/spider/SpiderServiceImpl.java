package com.mangobits.startupkit.social.spider;

import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostService;
import com.mangobits.startupkit.social.post.PostStatusEnum;
import com.mangobits.startupkit.social.post.PostTypeEnum;
import com.mangobits.startupkit.user.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class SpiderServiceImpl implements com.mangobits.startupkit.social.spider.SpiderService {


    @EJB
    private PostService postService;


    @EJB
    private UserService userService;


    @New
    @Inject
    private SpiderDAO spiderDAO;


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
                    .collect(Collectors.joining(")(?=.*", "(?=.*", ")"));

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

                        //check se ja nao foi processado
                        InfoUrl infoUrl = analyseUrl(url);

                        //cria o post
                        Post post = createPost(infoUrl, spider);

                        if(post != null){
                            listAdded.add(post);
                        }
                    }
                }
            }
        }

        return list;
    }



    private InfoUrl analyseUrl(String url) throws Exception {

        InfoUrl infoUrl = new InfoUrl();

        Document doc = Jsoup.connect(url).get();
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

        return infoUrl;
    }


    private Post createPost(InfoUrl infoUrl, Spider spider) throws Exception{

        List<Post> postsDB = postService.searchByNewsUrl(infoUrl.getUrl());
        if(postsDB == null || postsDB.size() == 0){

            Post post = new Post();
            post.setUserCreator(userService.generateCard(spider.getIdUserPostCreator()));
            post.setStatus(PostStatusEnum.PENDING);
            post.setCreationDate(new Date());
            post.setDesc(infoUrl.getDesc());
            post.setInfoUrl(infoUrl);
            post.setType(PostTypeEnum.NEWS);

            postService.save(post);

            return post;
        }

        return null;
    }



    @Override
    public List<Spider> listByStatus(SimpleStatusEnum status) throws Exception {
        return spiderDAO.search(new SearchBuilder()
                .appendParam("status", SimpleStatusEnum.ACTIVE)
                .build());
    }

}
