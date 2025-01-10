package com.beautytouch.beautytouch.entity;

public enum Role {


        USERS("users"),
        ADMIN("admin"),
        STUDIO("studio");
        private final String role;

        Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

