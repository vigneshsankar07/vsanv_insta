package com.cog.ananv.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by test on 14/10/17.
 */

public class ViewCategory_Model {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    public String getCategoryId() {
    return categoryId;
    }

    public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
    }

    public String getCategoryName() {
    return categoryName;
    }

    public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
    }
}
