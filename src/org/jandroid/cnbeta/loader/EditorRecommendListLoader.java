package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.EditorRecommend;
import org.json.simple.JSONObject;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EditorRecommendListLoader extends AbstractListLoader<EditorRecommend> {
//jQuery180013310679723508656_1377959872657({"status":"success","result":[{"title":"\u5e7f\u4e1c\u4f5b\u5c71\u653610\u5143\u4ee3\u4eba\u8d2d\u7968\u592b\u59bb\u4eca\u65e5\u88ab\u91ca\u653e","sid":"223581","url_show":"\/articles\/223581.htm","hometext_show_short":"\u5728\u7126\u6025\u7b49\u5f85\u534a\u5e74\u4e4b\u540e\uff0c\u66fe\u7ecf\u8f70\u52a8\u5168\u56fd\u7684\u201c\u5e7f\u4e1c\u6700\u5927\u5012\u5356\u706b\u8f66\u7968\u9ed1\u7a9d\u70b9\u201d\u4f5b\u5c71\u5c0f\u592b\u59bb\u4e00\u6848\u76ee\u524d\u7ec8\u4e8e\u6709\u4e86\u7ed3\u679c\u3002\u8b66\u65b9\u51b3\u5b9a\u5bf9\u4e8c\u4eba\u5904\u4ee5\u884c\u653f\u62d8\u755912\u5929\u7684\u5904\u7f5a\uff0c\u5e76..."},{"title":"[\u5df2\u5b8c\u7ed3]\u5fae\u8f6fBUILD 2013\u5f00\u573a\u4e3b\u9898\u6f14\u8bb2\u56fe\u6587\u76f4\u64ad","sid":"242581","url_show":"\/articles\/242581.htm","hometext_show_short":"\u5fae\u8f6f\u5c06\u4e8e\u5317\u4eac\u65f6\u95f427\u65e5\u96f6\u70b9\u5728\u65e7\u91d1\u5c71Moscone \r\nCenter\u5f00\u542f\u4eca\u5e74\u7684BUILD\u5927\u4f1a\uff0c\u4f1a\u671f\u5171\u8ba1\u4e24\u5929\uff0c\u8fd9\u662f\u5fae\u8f6f\u9762\u5411Windows\u5f00\u53d1\u8005\u7684\u91cd\u8981\u4f1a\u8bae\uff0c\u4f1a\u4e0a\u5c06\u63a2\u8ba8\u6709\u5173Windows \r\n8.1\u3001..."},{"title":"\u9b45\u65cfMX2 TD\u6b63\u5f0f\u53d1\u5e03  \u65e0\u9884\u88c5\u5e94\u7528 \u7cfb\u7edf\u7b80\u6d01\u7eaf\u51c0","sid":"242228","url_show":"\/articles\/242228.htm","hometext_show_short":"\u611f\u8c22\u65e0\u5948\u7684\u6295\u9012\u4eca\u5929\u9b45\u65cf\u4e00\u5927\u65e9\u5c31\u516c\u5e03\u4e86TD\u7248MX2\u7684\u6d88\u606f\uff0c\u672c\u6b21\u53d1\u5e03\u7684MX2 \r\nTD\u62e5\u670916G\u300132G\u300164G\u4e09\u4e2a\u7248\u672c\uff0c\u4ef7\u683c\u5206\u522b\u4e3a2299\u5143\u30012799\u5143\u30013699\u5143\uff0c\u4e09\u4e2a\u7248\u672c\u5c06\u540c\u65f6\u4e8e..."},{"title":"Windows 8.1\u9884\u89c8\u7248\u5347\u7ea7\u8fc7\u7a0b\u622a\u56fe\u6d41\u51fa","sid":"242209","url_show":"\/articles\/242209.htm","hometext_show_short":"\u4eceWindows 8\u5347\u7ea7\u5230Windows 8.1\u7684\u6d41\u7a0b\u622a\u56fe\u5df2\u4e8e\u4eca\u5929\u5728\u7f51\u4e0a\u66dd\u5149\uff0c\u63ed\u793a\u4e86\u4eceWindows Store\u5b89\u88c5Windows 8.1\u66f4\u65b0\u7684\u6240\u6709\u7ec6\u8282\u3002\u5728\u5355\u72ec\u5b89\u88c5\u5b8c\u4e00\u4e2a\u66f4\u65b0\u4e4b\u540e\uff0c\u7528\u6237\u5c4f\u5e55\u4e0a\u4f1a..."},{"title":"iPhone5S\u6444\u50cf\u5934\u53cc\u95ea\u5149\u706f\u9ad8\u6e05\u56fe\u66dd\u5149","sid":"242106","url_show":"\/articles\/242106.htm","hometext_show_short":"\u611f\u8c22T\u5ba2\u5728\u7ebf\u7684\u6295\u9012\u6628\u5929\u56fd\u5916\u7f51\u7ad9\u66dd\u5149\u4e86\u4e24\u5f20iPhone5S\u7684\u771f\u673a\u56fe\uff0c\u663e\u793aiPhone5S\u5c06\u914d\u5907\u53cc\u95ea\u5149\u706f\uff0c\u4eca\u5929\u5fae\u535a\u7528\u6237@palm\u5927\u53d4\u66dd\u5149\u4e86\u4e00\u5f20iPhone5S\u80cc\u90e8\u6444\u50cf\u5934\u548c\u53cc\u95ea\u5149\u706f..."},{"title":"\u7814\u7a76\u4eba\u5458\u5f00\u53d1\u65b0\u7cfb\u7edf \u8ba9\u544a\u5bc6\u8005\u7684\u6cc4\u9732\u884c\u52a8\u53d8\u5f97\u66f4\u5b89\u5168","sid":"242134","url_show":"\/articles\/242134.htm","hometext_show_short":"\u8fd1\u65e5\uff0c\u4e00\u79cd\u5168\u65b0\u57fa\u4e8eWeb\u7684\u6280\u672f\u5c06\u8ba9\u544a\u5bc6\u8005\u5728\u7f51\u7edc\u4e0a\u7684\u6570\u636e\u6cc4\u9732\u884c\u52a8\u53d8\u5f97\u66f4\u52a0\u7b80\u5355\u800c\u53c8\u5b89\u5168\u3002\u636e\u6089\uff0c\u6765\u81ea\u5fb7\u56fd\u7684\u4e00\u652f\u7814\u53d1\u56e2\u961f\u8fd1\u65e5\u5f00\u53d1\u51fa\u4e86\u4e00\u79cd\u57fa\u4e8e\u4e92\u8054\u7f51..."},{"title":"\u7f8e\u56fd\u6b63\u5f0f\u8d77\u8bc9\u65af\u8bfa\u767b","sid":"242045","url_show":"\/articles\/242045.htm","hometext_show_short":"\u7f8e\u56fd\u8054\u90a6\u68c0\u5bdf\u5b98\u5df2\u7ecf\u6b63\u5f0f\u4ee5\u201c\u95f4\u8c0d\u7f6a\u201d\u548c\u201c\u76d7\u7a83\u6216\u8f6c\u79fb\u653f\u5e9c\u8d22\u4ea7\u7f6a\u201d\u7f6a\u540d\uff0c\u5bf9\u201c\u68f1\u955c\u95e8\u201d\u544a\u5bc6\u8005\u7231\u5fb7\u534e\u00b7\u65af\u8bfa\u767b\u63d0\u8d77\u5211\u4e8b\u8bc9\u8bbc\u3002\u68c0\u5bdf\u5b98\u8868\u793a\uff0c\u65af\u8bfa\u767b\u4f5c\u4e3a..."},{"title":"[\u591a\u56fe]\u4e09\u661f\u5728\u4f26\u6566\u53d1\u5e03Galaxy S4 mini\u7b49\u65b0\u54c1","sid":"241986","url_show":"\/articles\/241986.htm","hometext_show_short":"\u4e09\u661f\u4eca\u5929\u5728\u82f1\u56fd\u4f26\u6566\u6b63\u5f0f\u53d1\u5e03Galaxy\u53caATIV\u7cfb\u52179\u6b3e\u65b0\u54c1\uff0c\u5176\u4e2d\u5305\u62ec\u5149\u5b66\u53d8\u7126\u667a\u80fd\u624b\u673aGalaxy S4 \r\nzoom\u3001\u6700\u9ad8\u5c4f\u5e55\u5206\u8fa8\u7387\u53d8\u5f62\u8d85\u6781\u672cATIV Q\u300110.1\u82f1\u5bf8\u8d85\u8584Windows 8\u5e73\u677f..."},{"title":"[\u56fe]\u795e\u5e99\u9003\u4ea1\u56e2\u961f\u98df\u8a00\uff1f512MB\u5185\u5b58\u7684WP8\u8bbe\u5907\u4f9d\u7136\u65e0\u6cd5\u8fd0\u884c","sid":"242001","url_show":"\/articles\/242001.htm","hometext_show_short":"\u6628\u5929Joe Belfiore\u548c\u795e\u5e99\u9003\u4ea1\u5f00\u53d1\u56e2\u961f\u5728\u6e38\u620f\u5b98\u65b9\u5fae\u535a\u4e0a\u8868\u793a\u8be5\u6b3e\u6e38\u620f\u5df2\u7ecf\u652f\u6301512MB\u5185\u5b58\u7684WP8\u8bbe\u5907\uff0c\u4f46\u662f\u4e8b\u5b9e\u4e0a\u5e76\u975e\u5982\u6b64\uff0c\u6709\u7f51\u53cb\u8868\u793aLumia 520\u8fd9\u6b3e\u8bbe\u5907\u4e0a\u5b89\u88c5..."},{"title":"[\u8ba8\u8bba]\u4f60\u5bb6\u4e61\u7684\u516c\u7528\u7535\u8bdd\u4ead\u8fd8\u597d\u5417\uff1f","sid":"241740","url_show":"\/articles\/241740.htm","hometext_show_short":"\u6211\u4e2a\u4eba\u8868\u793a\uff0c\u4ece\u6ca1\u7528\u8fc7\u4e00\u6b21\u516c\u5171\u7535\u8bdd\u4ead\u3002\u6211\u8001\u5bb6\u662f\u4e2a\u5c0f\u57ce\u5e02\uff0c\u5927\u6982\u662f\u572803\u5e74\u5de6\u53f3\u5f00\u59cb\u94fa\u6ee1\u6ee1\u5927\u8857\u7684\u7535\u8bdd\u4ead\uff0c\u4e8e\u6b64\u540c\u65f6\u6ee1\u5927\u8857\u7684\u5c0f\u5356\u90e8\u90fd\u63d0\u4f9b\u4e86\u516c\u8bdd\u670d\u52a1\uff0c..."}]})
    public EditorRecommendListLoader(int page) {
        super(Type.EDITOR_COMMEND, page);
    }

    @Override
    protected EditorRecommend newEntity(JSONObject jSONObject) {
        return new EditorRecommend(jSONObject);
    }
}
