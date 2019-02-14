package com.epicquotes.Helper;

/**
 * Created by mohahm01 on 30-09-2015.
 */
public class Story {
        String full_picture;
        int total_count;
        boolean can_like;
        boolean has_liked;
        String object_id;
        String message;
    public Story()
    {
        this.full_picture = null;
        this.total_count = 0;
        this.can_like=true;
        this.has_liked=false;
        this.object_id=null;
        this.message=null;

    }

        public String getFull_picture() {
            return full_picture;
        }
        public void setFull_picture(String full_picture) {
            this.full_picture = full_picture;
        }
        public int getTotal_count() {
            return total_count;
        }
        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public boolean getCan_like() {
            return this.can_like;
        }
        public void setCan_like(boolean can_like) {
            this.can_like=can_like;
        }

         public boolean getHas_liked() {
        return this.has_liked;
        }
        public void setHas_liked(boolean has_liked) {
        this.has_liked=has_liked;
        }
        public String getObject_id(){
            return object_id;
        }
        public void setObject_id(String object_id){this.object_id=object_id;}
        public void setMessage(String message){this.message=message;}
        public String getMessage(){return message;}


}


