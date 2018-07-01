package com.questionnaire;

public class User {
    private final String firstName;
    private final String lastName;
    private final String age;
    private final String position;

    public static class Builder{
        private String firstName;
        private String lastName;
        private String age;
        private String position;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setAge(String age){
            this.age = age;
            return this;
        }

        public Builder setPosition(String position){
            this.position = position;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.position = builder.position;
    }
}
