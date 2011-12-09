/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.migrationanalyzer.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public final class Tree<T extends Comparable<T>> implements Comparable<Tree<T>> {

    private final T head;

    private final TreeSet<Tree<T>> children = new TreeSet<Tree<T>>();

    private Tree<T> parent = null;

    private final HashMap<T, Tree<T>> locatorMap = new HashMap<T, Tree<T>>();

    public Tree(T head) {
        this.head = head;
        this.locatorMap.put(head, this);
    }

    public Tree<T> addChild(T child) {
        Tree<T> t = new Tree<T>(child);
        this.children.add(t);
        t.parent = this;
        this.locatorMap.put(child, t);
        return t;
    }

    public Tree<T> addChildIfAbsent(T element) {
        Tree<T> tmpTree = this.locatorMap.get(element);
        if (tmpTree == null) {
            tmpTree = addChild(element);
        }
        return tmpTree;
    }

    public T getHead() {
        return this.head;
    }

    public Tree<T> getTree(T element) {
        return this.locatorMap.get(element);
    }

    public boolean hasElemenent(T element) {
        return this.locatorMap.get(element) == null;
    }

    public Tree<T> getParent() {
        return this.parent;
    }

    public Collection<Tree<T>> getChildren() {
        return this.children;
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public boolean isFirstChildLeaf() {
        return (this.children.first()).children.size() == 0;
    }

    @Override
    public String toString() {
        return printTree(0);
    }

    public String printTree(int increment) {
        String s = "";
        String inc = "";
        for (int i = 0; i < increment; i++) {
            inc = inc + " ";
        }
        s = inc + this.head;
        for (Tree<T> child : this.children) {
            s = s + "\n" + child.printTree(increment + 2);
        }
        return s;
    }

    @Override
    public int compareTo(Tree<T> tree2) {
        return this.head.compareTo(tree2.head);
    }
}