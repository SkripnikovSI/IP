package info.kgeorgiy.java.advanced.implementor;

import java.io.File;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 * @version $$Id$$
 */
public interface Impler {
    /**
     * Produces code implementing class or interface specified by provided <tt>token</tt>.
     * <p>
     * Generated class full name should be same as full name of the type token with <tt>Impl</tt> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <tt>root</tt> directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to <tt>$root/java/util/ListImpl.java</tt>
     *
     *
     * @param token type token to create implementation for.
     * @param root root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     * generated.
     */
    void implement(Class<?> token, File root) throws ImplerException;
}
