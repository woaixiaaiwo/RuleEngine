package com.execution.engine;

public class Person {

    private Integer age;

    private Integer sex;

    private Integer score;

    private String des;

    public Person(Integer age, Integer sex, Integer score, String des) {
        this.age = age;
        this.sex = sex;
        this.score = score;
        this.des = des;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
