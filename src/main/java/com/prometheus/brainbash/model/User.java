package com.prometheus.brainbash.model;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(unique=true)
	private String username;
	
	@NotNull
	private String password;
	
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
	
	public Set<Role> getAuthorities() {
		return Set.of(role);
	}
	
	// Methods
	@Override
	public boolean equals(Object object) {
	    if (this == object) return true;
	    if (object == null || getClass() != object.getClass()) return false;
	    User user = (User) object;
	    return id != null && id.equals(user.getId());
	}
	
	@Override
	public int hashCode() {
	    return id != null ? id.hashCode() : 0;
	}

}

