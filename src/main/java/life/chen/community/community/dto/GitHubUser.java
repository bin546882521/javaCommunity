package life.chen.community.community.dto;

import lombok.Data;

@Data
public class GitHubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getBio() {
//        return bio;
//    }
//
//    public void setBio(String bio) {
//        this.bio = bio;
//    }
//
//    @Override
//    public String toString() {
//        return "GitHubUser{" +
//                "name='" + name + '\'' +
//                ", id=" + id +
//                ", bio='" + bio + '\'' +
//                '}';
//    }
}
