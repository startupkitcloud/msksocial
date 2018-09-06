package com.mangobits.startupkit.social.spider;

import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostService;
import com.mangobits.startupkit.social.spider.site.Site;
import com.mangobits.startupkit.social.spider.site.SiteService;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SpiderServiceImpl implements SpiderService {


    @EJB
    private SiteService siteService;


    @EJB
    private PostService postService;


    @EJB
    private UserService userService;


    @Override
    public void goSpider() throws Exception {

        //carrega todos os sites ativos
        List<Site> listSites = siteService.listByStatus(SimpleStatusEnum.ACTIVE);

        //faz a leitura
        if(listSites != null){
            for (Site site : listSites){

                //busca as noticias
                searchNews(site);

                //adiciona os posts
            }
        }

    }


    private List<String> searchNews(Site site) throws Exception{

        List<String> list = new ArrayList<>();

        Document doc = Jsoup.connect(site.getUrl()).get();

        Elements els = doc.select("a[href]");

        if(els != null){

            String patternStr = site.getUrlPatterns().stream()
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
                    //check se ja nao foi processado
                    InfoUrl infoUrl = analyseUrl(url);

                    //cria o post
                    createPost(infoUrl, site);
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


    private void createPost(InfoUrl infoUrl, Site site) throws Exception{

        List<Post> postsDB = postService.searchByNewsUrl(infoUrl.getUrl());
        if(postsDB == null || postsDB.size() == 0){

            Post post = new Post();
            post.setUserCreator(userService.generateCard(site.getIdUserPostCreator()));
            post.setStatus(SimpleStatusEnum.BLOCKED);
            post.setCreationDate(new Date());
            post.setDesc(infoUrl.getDesc());
            post.setInfoUrl(infoUrl);
            post.setType("NEWS");

            postService.save(post);
        }
    }
}
