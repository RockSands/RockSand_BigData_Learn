package com.restful;

public class Student {

    private long id;
    private int age;
    private String name;

    public Student() {}

    public Student(long id, int age, String name) {
      this.id = id;
      this.age = age;
      this.name = name;
    }

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "Student{" +
       "id=" + id +
       ", age=" + age +
       ", name='" + name + '\'' +
       '}';
    }
  }