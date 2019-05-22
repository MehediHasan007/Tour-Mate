package com.example.tourmate.Class;

public class Upload {

    private String uploadId;
    private String imageUrl;
    private String imageTitle;

    public Upload() {
    }

    public Upload(String imageUrl, String imageTitle) {
        this.imageUrl = imageUrl;
        this.imageTitle = imageTitle;
    }

    public Upload(String uploadId, String imageUrl, String imageTitle) {
        this.uploadId = uploadId;
        this.imageUrl = imageUrl;
        this.imageTitle = imageTitle;
    }

    public String getUploadId() {
        return uploadId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }
}
