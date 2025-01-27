/*
 * ecoCode SonarQube Plugin
 * Copyright (C) 2020-2021 Snapp' - Université de Pau et des Pays de l'Adour
 * mailto: contact@ecocode.io
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.ecocode.java.checks.helpers;

import org.sonar.plugins.java.api.tree.*;

import java.util.ArrayList;

public class TreeHelper {

    private TreeHelper() {
        // Class cannot be instantiated
    }

    /**
     * Return the full qualified name of an expression. "com.package.name" for a package for example.
     * @param tree the tree from which the name must be built.
     * @return the full qualified name of the tree
     */
    public static String fullQualifiedName(Tree tree) {
        if (tree.is(Tree.Kind.IDENTIFIER)) {
            return ((IdentifierTree) tree).name();
        } else if (tree.is(Tree.Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree m = (MemberSelectExpressionTree) tree;
            return fullQualifiedName(m.expression()) + "." + m.identifier().name();
        }
        throw new UnsupportedOperationException(String.format("Kind/Class '%s' not supported", tree.getClass()));
    }

    /**
     * Check if the value of the given expression is equal to a given value.
     *
     * @param argument the argument to check
     * @param treesToReport the tree to report the issue
     * @param valueToControl the value for which we report an issue if the argument is equal to the value
     */
    public static void literalValueControl(ExpressionTree argument, ArrayList<Tree> treesToReport, int valueToControl) {
        try {
            switch (argument.symbolType().fullyQualifiedName()) {
                case "int":
                    if (argument.asConstant().isPresent() && ((Integer) argument.asConstant().get() == valueToControl)) {
                        treesToReport.add(argument);
                    } else if (argument.is(Tree.Kind.INT_LITERAL)) {
                        LiteralTree lit = (LiteralTree) argument;
                        if (Integer.valueOf(lit.value()) == valueToControl) {
                            treesToReport.add(argument);
                        }
                    }
                    break;
                case "float":
                    if (argument.asConstant().isPresent() && ((Float) argument.asConstant().get() == valueToControl)) {
                        treesToReport.add(argument);
                    } else if (argument.is(Tree.Kind.FLOAT_LITERAL)) {
                        LiteralTree lit = (LiteralTree) argument;
                        if (Float.valueOf(lit.value()) == valueToControl) {
                            treesToReport.add(argument);
                        }
                    }
                    break;
                case "double":
                    if (argument.asConstant().isPresent() && ((Double) argument.asConstant().get() == valueToControl)) {
                        treesToReport.add(argument);
                    } else if (argument.is(Tree.Kind.DOUBLE_LITERAL)) {
                        LiteralTree lit = (LiteralTree) argument;
                        if (Double.valueOf(lit.value()) == valueToControl) {
                            treesToReport.add(argument);
                        }
                    }
                    break;
                case "long":
                    if (argument.asConstant().isPresent() && ((Long) argument.asConstant().get() == valueToControl)) {
                        treesToReport.add(argument);
                    } else if (argument.is(Tree.Kind.LONG_LITERAL)) {
                        LiteralTree lit = (LiteralTree) argument;
                        if (Long.valueOf(lit.value()) == valueToControl) {
                            treesToReport.add(argument);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            // Ignore this case: occurs only if "Integer.valueOf(x)" method fails because the argument is an Long (may
            // happen, don't know why). If it is the case, the argument value is not equal to 0 since Integer.valueOf(0)
            // will always succeed
        }
    }
}
