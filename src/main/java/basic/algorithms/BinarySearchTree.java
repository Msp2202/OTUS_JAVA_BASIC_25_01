package basic.algorithms;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> implements SearchTree<T> {

    private Node<T> root;

    public BinarySearchTree(List<T> sortedList) {
        if (sortedList == null || sortedList.isEmpty()) {
            this.root = null;
        } else {
            // Создаем копию, чтобы не менять исходный список
            List<T> copy = new ArrayList<>(sortedList);
            this.root = buildBalancedTree(copy, 0, copy.size() - 1);
        }
    }

    /**
     * Рекурсивное построение сбалансированного дерева из отсортированного списка
      */

    private Node<T> buildBalancedTree(List<T> sortedList, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (start + end) / 2;
        Node<T> node = new Node<>(sortedList.get(mid));

        node.left = buildBalancedTree(sortedList, start, mid - 1);
        node.right = buildBalancedTree(sortedList, mid + 1, end);

        return node;
    }

    @Override
    public T find(T element) {
        return findRecursive(root, element);
    }

    /**
     * Рекурсивный поиск элемента в дереве
     */
    private T findRecursive(Node<T> node, T element) {
        if (node == null) {
            return null;
        }

        int cmp = element.compareTo(node.value);

        if (cmp == 0) {
            return node.value;
        } else if (cmp < 0) {
            return findRecursive(node.left, element);
        } else {
            return findRecursive(node.right, element);
        }
    }

    @Override
    public List<T> getSortedList() {
        List<T> result = new ArrayList<>();
        inOrderTraversal(root, result);
        return result;
    }

    /**
     * Обход дерева в порядке возрастания (левый-корень-правый)
     */
    private void inOrderTraversal(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add(node.value);
            inOrderTraversal(node.right, result);
        }
    }

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }
}