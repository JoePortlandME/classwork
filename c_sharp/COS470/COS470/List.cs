using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace COS470
{
    class List<T> : IEnumerable<T>
    {

        // Properties for head node and length of List

        Node<T> head { get; set; }
        private int length { get; set; }

        // Node used to hold each data item

        public class Node<T>
        {
            // Properties of each Node: data, next node and previous node

            public T data { get; set; }
            public Node<T> next { get; set; }
            public Node<T> prev { get; set; }

            public Node(T dataItem)
            {
                data = dataItem;
            }
        }

        private int Size()
        {
            return length;
        }

        public bool Add(T anEntry)
        {
            Add(length, anEntry);
            return true;
        }

        public void Add(int index, T anEntry)
        {
            if (index < 0 || index > length)
                throw new IndexOutOfRangeException(index.ToString());

            if (index == 0)
                AddFirst(anEntry);

            else
            {
                Node<T> node = GetNode(index - 1);
                AddAfter(node, anEntry);
            }
        }

        private Node<T> GetNode(int index)
        {
            Node<T> node = head;

            for (int i = 0; i < index && node != null; i++)
                node = node.next;
            return node;
        }

        public void AddFirst(T item)
        {
            Node<T> newNode = new Node<T>(item);
            if (length > 0)
            {
                head.prev = newNode;
                newNode.next = head;
            }
            head = newNode;

            length++;
        }

        public void AddAfter(Node<T> node, T item)
        {
            Node<T> newNode = new Node<T>(item);
            newNode.prev = node;
            if (node.next != null)
            {
                newNode.next = node.next;
                newNode.next.prev = newNode;
            }
            node.next = newNode;

            length++;
        }

        public int IndexOf(T target)
        {
            Node<T> node = head;
            for (int i = 0; i < length; i++)
            {
                if (node.Equals(target))
                    return i;
                node = node.next;
            }
            return -1;
        }

        public bool Contains(T target)
        {
            if (head == null)
                return false;
            Node<T> node = head;
            while (node != null)
            {
                if (node.data.Equals(target))
                    return true;
                node = node.next;
            }
            return false;
        }

        public bool Remove(T element)
        {
            if (head == null)
                return false;

            Node<T> node = head;

            if (element.Equals(head.data))
                if (head.next != null)
                {
                    head.next.prev = null;
                    head = head.next;
                    length--;
                    return true;
                }
                else
                {
                    head = null;
                    length--;
                    return true;
                }
            for (int i = 0; i < length && node != null; i++)
            {
                if (node.data.Equals(element))
                {
                    if (node.next != null)
                        node.next.prev = node.prev;
                    if (node.prev != null)
                        node.prev.next = node.next;
                    length--;
                    return true;
                }
                node = node.next;
            }

            return false;
        }

        public bool IsEmpty()
        {
            if (head == null)
                return true;
            return false;
        }

        public IEnumerator<T> GetEnumerator()
        {
            Node<T> currNode = head;

            for (int i = 0; i < length; i++)
            {
                yield return currNode.data;
                currNode = currNode.next;
            }
        }


        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }
    }
}
