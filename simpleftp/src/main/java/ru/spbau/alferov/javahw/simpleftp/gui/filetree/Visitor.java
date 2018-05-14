package ru.spbau.alferov.javahw.simpleftp.gui.filetree;

/**
 * This is an interface for walking through remote files in pre-order.
 * @see <a href="https://sourcemaking.com/design_patterns/visitor">Visitor design pattern.</a>
 */
public interface Visitor {
    /**
     * Action performed on a directory.
     */
    void visitDirectory(RemoteDirectory directory) throws VisitorException;

    /**
     * Action performed on a file.
     */
    void visitFile(RemoteFile file) throws VisitorException;

    /**
     * Action performed on an arbitrary node. Might be overridden in purpose to do some action before or after actual visit.
     */
    default void visit(RemoteFileNode node) throws VisitorException {
        node.accept(this);
    }
}
