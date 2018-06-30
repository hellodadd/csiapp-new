package com.android.csiapp.Crime.utils.tree;

/**
 * Information about the node.
 * 
 * @param <T>
 *            type of the id for the tree
 */
public class TreeNodeInfo<T> {
    private final T id;
    private final int level;
    private final boolean withChildren;
    private final boolean visible;
    private final boolean expanded;
    private final String description;

    /**
     * Creates the node information.
     * 
     * @param id
     *            id of the node
     * @param level
     *            level of the node
     * @param withChildren
     *            whether the node has children.
     * @param visible
     *            whether the tree node is visible.
     * @param expanded
     *            whether the tree node is expanded
     * 
     */
    public TreeNodeInfo(final T id, final int level,
            final boolean withChildren, final boolean visible,
            final boolean expanded, final String description) {
        super();
        this.id = id;
        this.level = level;
        this.withChildren = withChildren;
        this.visible = visible;
        this.expanded = expanded;
        this.description = description;
    }

    public T getId() {
        return id;
    }

    public boolean isWithChildren() {
        return withChildren;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TreeNodeInfo [id=" + id + ", level=" + level
                + ", withChildren=" + withChildren + ", visible=" + visible
                + ", expanded=" + expanded + ", description=" + description +"]";
    }

}