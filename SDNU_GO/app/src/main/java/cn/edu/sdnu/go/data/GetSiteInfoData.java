package cn.edu.sdnu.go.data;

import java.util.List;

/**
 * Created by jsj1996m on 2016/8/28.
 */
public class GetSiteInfoData {
    String Name;
    int SiteID;
    double Lng;
    double Lon;
    String SiteDescription;
    List<ImageInfoData> ImageInfo;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getSiteID() {
        return SiteID;
    }

    public void setSiteID(int siteID) {
        SiteID = siteID;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public String getSiteDescription() {
        return SiteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        SiteDescription = siteDescription;
    }

    public List<ImageInfoData> getImageInfo() {
        return ImageInfo;
    }

    public void setImageInfo(List<ImageInfoData> imageInfo) {
        ImageInfo = imageInfo;
    }

    public class ImageInfoData {
        String content;
        String contentX;
        String altinfo;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentX() {
            return contentX;
        }

        public void setContentX(String contentX) {
            this.contentX = contentX;
        }

        public String getAltinfo() {
            return altinfo;
        }

        public void setAltinfo(String altinfo) {
            this.altinfo = altinfo;
        }
    }

}
