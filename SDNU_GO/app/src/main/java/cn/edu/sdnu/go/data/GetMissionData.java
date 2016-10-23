package cn.edu.sdnu.go.data;

import java.util.List;

/**
 * Created by jsj1996m on 2016/8/28.
 */
public class GetMissionData {
    int result;
    List<MissionData> list;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<MissionData> getList() {
        return list;
    }

    public void setList(List<MissionData> list) {
        this.list = list;
    }

    public class MissionData{
        String Name;
        String TaskID;
        String SiteList;
        String Content;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getTaskID() {
            return TaskID;
        }

        public void setTaskID(String taskID) {
            TaskID = taskID;
        }

        public String getSiteList() {
            return SiteList;
        }

        public void setSiteList(String siteList) {
            SiteList = siteList;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }
    }

}
