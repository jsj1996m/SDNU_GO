package cn.edu.sdnu.go.data;

import java.util.List;

/**
 * Created by jsj1996m on 2016/8/27.
 */
public class GetPointData {

    int result;
    List<PointData> list;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }


    public List<PointData> getList() {
        return list;
    }

    public void setList(List<PointData> list) {
        this.list = list;
    }

    public class PointData {
        String SiteName;
        int SiteID;
        double Lat;
        double Lng;

        public double getLng() {
            return Lng;
        }

        public void setLng(double lng) {
            Lng = lng;
        }

        public String getSiteName() {
            return SiteName;
        }

        public void setSiteName(String siteName) {
            SiteName = siteName;
        }

        public int getSiteID() {
            return SiteID;
        }

        public void setSiteID(int siteID) {
            SiteID = siteID;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

    }

}
