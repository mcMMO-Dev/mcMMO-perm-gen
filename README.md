# mcMMO-perm-gen

mcMMO-perm-gen is a tool for automatically generating a list of permissions from a plugin.yml written for use with mcMMO

## Usage

<pre>java -jar mcMMO-perms-gen.jar [path to plugin.yml] [path to main.template] [path to child.template] [path to parent.template] [path to child_list.template]</pre>

## plugin.yml

Any valid plugin.yml will work with this tool, however to any permission node you may add the following:

* noparse: true
 * __This will prevent the tool from processing this node__
* wiki: _String_
 * __This will provide additional information for the \_\_WIKI\_INFO\_\_ key__

## Template

Inside the main template, \_\_CHILD\_NODES\_\_ will include the Child Template for each child node found in the plugin.yml, \_\_PARENT\_NODES\_\_ will include the Parent Template for each parent node found in the plugin.yml, \_\_CHILD\_LIST\_\_ will list all child nodes in the root separated by newline, \_\_PARENT\_LIST\_\_ will list all parent nodes in the root separated by newline.

Inside the Child and Child List Templates, the node will be formatted with the provided information from the plugin.yml

Inside the Parent Template, the node will be formatted with the provided information from the plugin.yml, additionally \_\_CHILD\_LIST\_FORMATTED\_\_ will include the Child List Template for that parent node.

### Main Template Keys

* \_\_CHILD\_NODES\_\_
* \_\_PARENT\_NODES\_\_
* \_\_CHILD\_LIST\_\_
* \_\_PARENT\_LIST\_\_

### Child Template Keys

* \_\_NODE\_\_
* \_\_DEFAULT\_INHERITANCE\_\_
* \_\_WIKI\_INFO\_\_

### Parent Template Keys

* \_\_NODE\_\_
* \_\_DEFAULT\_INHERITANCE\_\_
* \_\_WIKI\_INFO\_\_
* \_\_CHILD\_LIST\_FORMATTED\_\_

### Child List Template Keys

* \_\_NODE\_\_
* \_\_DEFAULT\_INHERITANCE\_\_
* \_\_WIKI\_INFO\_\_

## Compiling

Maven 3 is required to compile mcMMO-perm-gen