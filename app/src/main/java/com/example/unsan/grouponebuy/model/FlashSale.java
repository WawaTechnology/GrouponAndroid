package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class FlashSale  implements Serializable{


        @JsonProperty("state")
        private Integer state;
        @JsonProperty("productList")
        private List<Product> productList = null;
        @JsonProperty("_id")
        private String id;
        @JsonProperty("name_ch")
        private String nameCh;
        @JsonProperty("name_en")
        private String nameEn;
        @JsonProperty("image")
        private String image;
        @JsonProperty("deadline")
        private String deadline;


        @JsonProperty("state")
        public Integer getState() {
            return state;
        }

        @JsonProperty("state")
        public void setState(Integer state) {
            this.state = state;
        }

        @JsonProperty("productList")
        public List<Product> getProductList() {
            return productList;
        }

        @JsonProperty("productList")
        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

        @JsonProperty("_id")
        public String getId() {
            return id;
        }

        @JsonProperty("_id")
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("name_ch")
        public String getNameCh() {
            return nameCh;
        }

        @JsonProperty("name_ch")
        public void setNameCh(String nameCh) {
            this.nameCh = nameCh;
        }

        @JsonProperty("name_en")
        public String getNameEn() {
            return nameEn;
        }

        @JsonProperty("name_en")
        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        @JsonProperty("image")
        public String getImage() {
            return image;
        }

        @JsonProperty("image")
        public void setImage(String image) {
            this.image = image;
        }

        @JsonProperty("deadline")
        public String getDeadline() {
            return deadline;
        }

        @JsonProperty("deadline")
        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }


}
