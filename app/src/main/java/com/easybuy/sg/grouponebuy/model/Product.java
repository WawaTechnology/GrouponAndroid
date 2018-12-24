package com.easybuy.sg.grouponebuy.model;

import android.graphics.drawable.Drawable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)



public class Product implements Serializable{



        @JsonProperty("imageDisplay")
        private List<String> imageDisplay = null;
       // @JsonProperty("categorySpecial")

    public boolean isOnShelf() {
        return isOnShelf;
    }

    public void setOnShelf(Boolean onShelf) {
        isOnShelf = onShelf;
    }

    //  private List<Object> categorySpecial = null;
    @JsonProperty("isAttention")
    private boolean isAttention;
    @JsonProperty("isOnShelf")
    private Boolean isOnShelf;

    public Boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }


    @JsonProperty("_id")
        private String id;
        @JsonProperty("name_ch")
        private String nameCh;

    public Boolean getOnShelf() {
        return isOnShelf;
    }



    @JsonProperty("name_en")
        private String nameEn;
        @JsonProperty("origin_ch")
        private String originCh;
        @JsonProperty("origin_en")
        private String originEn;
        @JsonProperty("description_ch")
        private String descriptionCh;
        @JsonProperty("description_en")
        private String descriptionEn;
        @JsonProperty("specification_ch")
        private String specificationCh;
        @JsonProperty("specification_en")
        private String specificationEn;
     //@JsonProperty("categorySpecial")
//List<Object> categorySList;

      //  @JsonProperty("categorySpecial")
    //   List<String> categorySpecialList;
        @JsonProperty("unit_ch")
        private String unitCh;
        @JsonProperty("unit_en")
        private String unitEn;

  /*  public List<String> getSpecialCategoryList() {
        return categorySpecialList;
    }

    public void setSpecialCategoryList(List<String> categorySpecialList) {
        this.categorySpecialList = categorySpecialList;
    }
    */

    @JsonProperty("category")

        private Category category;
        @JsonProperty("priceOriginal")
        private Double priceOriginal;
        @JsonProperty("price")
        private Double price;

    public String getsKU() {
        return sKU;
    }

    public void setsKU(String sKU) {
        this.sKU = sKU;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

   /* public List<Object> getCategorySList() {
        return categorySList;
    }

    public void setCategorySList(List<Object> categorySList) {
        this.categorySList = categorySList;
    }
    */


    @JsonProperty("stock")


        private Integer stock;
    @JsonProperty("SKU")
    private String sKU;
    @JsonProperty("supplier")
    private String supplier;






    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    @JsonProperty("imageCover")
        private String imageCover;
        private int totalNumber;

      //  private final static long serialVersionUID = -2322634913455818838L;

        @JsonProperty("imageDisplay")
        public List<String> getImageDisplay() {
            return imageDisplay;
        }

        @JsonProperty("imageDisplay")
        public void setImageDisplay(List<String> imageDisplay) {
            this.imageDisplay = imageDisplay;
        }

      /*  @JsonProperty("categorySpecial")
        public List<Object> getCategorySpecial() {
            return categorySpecial;
        }

        @JsonProperty("categorySpecial")
        public void setCategorySpecial(List<Object> categorySpecial) {
            this.categorySpecial = categorySpecial;
        }
        */



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

        @JsonProperty("origin_ch")
        public String getOriginCh() {
            return originCh;
        }

        @JsonProperty("origin_ch")
        public void setOriginCh(String originCh) {
            this.originCh = originCh;
        }

        @JsonProperty("origin_en")
        public String getOriginEn() {
            return originEn;
        }

        @JsonProperty("origin_en")
        public void setOriginEn(String originEn) {
            this.originEn = originEn;
        }

        @JsonProperty("description_ch")
        public String getDescriptionCh() {
            return descriptionCh;
        }

        @JsonProperty("description_ch")
        public void setDescriptionCh(String descriptionCh) {
            this.descriptionCh = descriptionCh;
        }

        @JsonProperty("description_en")
        public String getDescriptionEn() {
            return descriptionEn;
        }

        @JsonProperty("description_en")
        public void setDescriptionEn(String descriptionEn) {
            this.descriptionEn = descriptionEn;
        }

        @JsonProperty("specification_ch")
        public String getSpecificationCh() {
            return specificationCh;
        }

        @JsonProperty("specification_ch")
        public void setSpecificationCh(String specificationCh) {
            this.specificationCh = specificationCh;
        }

        @JsonProperty("specification_en")
        public String getSpecificationEn() {
            return specificationEn;
        }

        @JsonProperty("specification_en")
        public void setSpecificationEn(String specificationEn) {
            this.specificationEn = specificationEn;
        }

        @JsonProperty("unit_ch")
        public String getUnitCh() {
            return unitCh;
        }

        @JsonProperty("unit_ch")
        public void setUnitCh(String unitCh) {
            this.unitCh = unitCh;
        }

        @JsonProperty("unit_en")
        public String getUnitEn() {
            return unitEn;
        }

        @JsonProperty("unit_en")
        public void setUnitEn(String unitEn) {
            this.unitEn = unitEn;
        }

        @JsonProperty("category")
        public Category getCategory() {
            return category;
        }

        @JsonProperty("category")
        public void setCategory(Category category) {
            this.category = category;
        }

        @JsonProperty("priceOriginal")
        public Double getPriceOriginal() {
            return priceOriginal;
        }

        @JsonProperty("priceOriginal")
        public void setPriceOriginal(Double priceOriginal) {
            this.priceOriginal = priceOriginal;
        }

        @JsonProperty("price")
        public Double getPrice() {
            return price;
        }

        @JsonProperty("price")
        public void setPrice(Double price) {
            this.price = price;
        }

        @JsonProperty("stock")
        public Integer getStock() {
            return stock;
        }

        @JsonProperty("stock")
        public void setStock(Integer stock) {
            this.stock = stock;
        }









        @JsonProperty("imageCover")
        public String getImageCover() {
            return imageCover;
        }

        @JsonProperty("imageCover")
        public void setImageCover(String imageCover) {
            this.imageCover = imageCover;
        }



        @Override
        public boolean equals (Object object) {
            boolean result = false;
            if (object == null || object.getClass() != getClass()) {
                result = false;
            } else {
               Product product = (Product) object;
                if (this.id.equals(product.getId())   && this.totalNumber == product.getTotalNumber()) {
                    result = true;
                }
            }
            return result;
        }

    }



