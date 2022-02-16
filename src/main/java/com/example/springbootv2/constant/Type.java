package com.example.springbootv2.constant;

public enum Type {
    TAG("tag"),
    CATEGORY("category"),
    ARTICLE("post"),
    PUBLISH("publish"),
    PAGE("page"),
    DRAFT("draft"),
    LINK("link"),
    IMAGE("image"),
    FILE("file"),
    CSRF_TOKEN("csrf_token"),
    COMMENTS_FREQUENCY("comments:frequency"),
    PHOTO("photo"),
    COMMENTS_PASSED_STATUS("approved"),

    /**
     * 附件存放的URL，默认为网站地址，如集成第三方则为第三方CDN域名
     */
    ATTACH_URL("attach_url"),

    /**
     * 网站要过滤，禁止访问的ip列表
     */
    BLOCK_IPS("site_block_ips");

    private String type;
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }
    Type(String type){
        this.type = type;
    }

}
