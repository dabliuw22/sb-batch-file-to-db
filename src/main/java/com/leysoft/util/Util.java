
package com.leysoft.util;

public class Util {

    private Util() {
    }

    public class SqlConstant {

        private SqlConstant() {
        }

        public static final String INSERT_PERSON =
                "insert into persons(name, birthday, age) values (:name, :birthday, :age)";
    }
}
