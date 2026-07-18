import lombok.Getter;

public class DoublyLinkedListNode {
    @Getter
    private final int key;
    public DoublyLinkedListNode prev;
    public DoublyLinkedListNode next;

    public DoublyLinkedListNode(int key)
    {
        this.key = key;
        this.prev = null;
        this.next = null;
    }
}
