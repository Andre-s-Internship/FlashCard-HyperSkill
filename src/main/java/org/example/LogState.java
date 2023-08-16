package org.example;

public enum LogState {
    INPUT {
        @Override
        public String toString() {
            return "INPUT: ";
        }
    }, OUTPUT {
        @Override
        public String toString() {
            return "OUTPUT: ";
        }
    }
}
