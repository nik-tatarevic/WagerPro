package com.wagerpro.userservice.domain.model;

public enum Role {
	USER("ROLE_USER"), 
	ADMIN("ROLE_ADMIN");

	private final String authority;

	Role(String authority) {
		this.authority = authority;
	}

	public String authority() {
		return authority;
	}

	public static Role fromAuthority(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("User role cannot be blank.");
		}
		for (Role role : values()) {
			if (role.authority.equalsIgnoreCase(value) || role.name().equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Unsupported role: " + value);
	}
}
