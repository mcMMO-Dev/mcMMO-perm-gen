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
		// We need to set the inheritance of all children if they are unset to this one's if we are set
		if(inheritance != Inheritance.UNSET) {
			recursiveInheritanceSet(this);
		}
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

	private void recursiveInheritanceSet(Permission parent) {
		System.out.println("Recursive set for " + parent.node + " to " + parent.inheritance);
		for(Permission child : parent.children) {
			if(Inheritance.greaterThan(parent.inheritance, child.inheritance)) {
				child.inheritance = parent.inheritance;
			}
			if(child.isParent()) {
				recursiveInheritanceSet(child);
			} else {
				System.out.println("Set child " + child.node + " to " + parent.inheritance);
			}
		}
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
		TRUE(4), FALSE(1), OP(3), NOT_OP(2), UNSET(0);

		private int weight;

		private Inheritance(int weight) {
			this.weight = weight;
		}

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
				case UNSET:
					return "False";
				default:
					return "Unknown";
			}
		}

		public static boolean greaterThan(Inheritance parent, Inheritance child) {
			return parent.weight > child.weight;
		}

		public static Inheritance fromYaml(String input) {
			if(input.equalsIgnoreCase("true")) {
				return TRUE;
			} else if(input.equalsIgnoreCase("false")){
				return FALSE;
			} else if(input.equalsIgnoreCase("op")) {
				return OP;
			} else if(input.equalsIgnoreCase("not op")) {
				return NOT_OP;
			} else {
				return UNSET;
			}
		}
	}
}
