package com.gmail.nossr50.mcmmopermgen;

import java.util.Set;
import java.util.TreeSet;

public class Permission implements Comparable<Permission> {
	private String node;
	private String wiki;
	private boolean parent;
	private Set<Permission> children;

	public Permission(String node, String wiki) {
		this.node = node;
		this.wiki = wiki;
		this.parent = false;
	}

	public Permission(String node, String wiki, Set<Permission> children) {
		this.node = node;
		this.wiki = wiki;
		this.parent = true;
		this.children = new TreeSet<Permission>(children);
	}

	public String getNode() {
		return node;
	}

	public String getWiki() {
		return wiki;
	}

	public boolean isParent() {
		return parent;
	}

	public Set<Permission> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		String value = "[node=" + node + ", wiki=" + wiki + ", parent=" + parent;
		if(isParent()) {
			value += ", children=[";
			for(Permission child : children) {
				value += child.getNode();
			}
			value += "]";
		}
		value += "]";
		return value;
	}

	@Override
	public int compareTo(Permission other) {
		return node.compareTo(other.getNode());
	}
}
