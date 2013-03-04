package com.gmail.nossr50.mcmmopermgen;

import java.util.Set;
import java.util.TreeSet;

public class Permission implements Comparable<Permission> {
	private String node;
	private String wiki;
	private boolean parent;
	private Inheritance inheritance;
	private Set<Permission> children;

	public Permission(String node, String wiki, Inheritance inheritance) {
		this.node = node;
		this.wiki = wiki;
		this.inheritance = inheritance;
		this.parent = false;
	}

	public Permission(String node, String wiki, Inheritance inheritance, Set<Permission> children) {
		this.node = node;
		this.wiki = wiki;
		this.inheritance = inheritance;
		this.parent = true;
		this.children = new TreeSet<Permission>(children);
	}

	public String getNode() {
		return node;
	}

	public String getWiki() {
		return wiki;
	}

	public Inheritance getInheritance() {
		return inheritance;
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

	public enum Inheritance {
		TRUE, FALSE, OP, NOT_OP;

		@Override
		public String toString() {
			switch(this) {
				case TRUE:
					return "True";
				case FALSE: 
					return "False";
				case OP:
					return "Op";
				case NOT_OP:
					return "Not Op";
				default:
					return "Unknown";
			}
		}

		public static Inheritance fromYaml(String input) {
			if(input.equalsIgnoreCase("true")) {
				return TRUE;
			} else if(input.equalsIgnoreCase("op")) {
				return OP;
			} else if(input.equalsIgnoreCase("not op")) {
				return NOT_OP;
			} else {
				return FALSE;
			}
		}
	}
}
