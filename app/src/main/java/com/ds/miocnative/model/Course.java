package com.ds.miocnative.model;

public class Course {

    private int id;
    private String name;
    private String title;
    private String hours;
    private String description;
    private String posterSrc;
    private String price;
    private int status;
    private Integer status_of_access;

    public Course(int id, String title, String hours, String price, String posterSrc) {
        this.id = id;
        this.title = title;
        this.hours = hours;
        this.price = price;
        this.posterSrc = posterSrc;
    }

    public Course(int id, String name, String title, String hours, String description, String posterSrc, String price, int status, Integer status_of_access) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.hours = hours;
        this.description = description;
        this.posterSrc = posterSrc;
        this.price = price;
        this.status = status;
        this.status_of_access = status_of_access;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterSrc() {
        return posterSrc;
    }

    public void setPosterSrc(String posterSrc) {
        this.posterSrc = posterSrc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getStatus_of_access() {
        return status_of_access;
    }

    public void setStatus_of_access(Integer status_of_access) {
        this.status_of_access = status_of_access;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hours='" + hours + '\'' +
                ", description='" + description + '\'' +
                ", posterSrc='" + posterSrc + '\'' +
                ", price='" + price + '\'' +
                ", status=" + status +
                ", status_of_access="+ status_of_access +
                '}';
    }
}
