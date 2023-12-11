package me.jeyzer.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Search<E> {

    /*========= STATIC =========*/
    public static final Search NO_RESULT = new Search<>();

    public static <E> Search<E> by(@NotNull Supplier<E> supplier) {
        return of(supplier.get());
    }

    public static <E> Search<E> of(@Nullable E element) {
        return element == null ? NO_RESULT : new Search<>(element);
    }

    public static <E> E ensure(E element, E alternative) {
        return element == null ? alternative : element;
    }

    public static <E> E exceptional(Supplier<E> supplier, Supplier<E> def) {
        return Search.ensure(exceptional(supplier), def);
    }

    public static <E> E exceptional(Supplier<E> supplier, E def) { // Style for lazy coding
        return Search.ensure(exceptional(supplier), def);
    }

    public static <E> E exceptional(Supplier<E> supplier) { // Style for lazy coding
        try {
            return supplier.get();
        }catch (Exception exception) {
            return null;
        }
    }

    public static <E> E ensure(E def, Supplier<E> elseSupply) {
        return def == null ? elseSupply == null ? null : elseSupply.get() : def;
    }

    public static <V, E> V ensure(E def, Function<E, V> ifExists, V alternative) {
        return def == null ? alternative : ifExists == null ? null : ifExists.apply(def);
    }

    public static <V, E> V expression(E def, Function<E, V> ifExists, Supplier<V> orElseSupply) {
        return def == null ? orElseSupply == null ? null : orElseSupply.get() : ifExists == null ? null : ifExists.apply(def);
    }
    /*===========================*/

    /*========= INSTANCE =========*/
    private Object element;
    private boolean lockElse;

    private Search() {}

    private Search(@NotNull E element) {
        this.element = element;
    }

    @NotNull
    public Search<E> exists(@NotNull Runnable runnable) {
        if (this == NO_RESULT)
            return this;

        Objects.requireNonNull(runnable);

        if (!exists())
            return this;

        runnable.run();
        return this;
    }

    @NotNull
    public Search<E> exists(@NotNull Consumer<E> consumer) {
        if (this == NO_RESULT)
            return this;

        if (!exists())
            return this;

        consumer.accept((E) element);
        return this;
    }

    @NotNull
    public Search<E> consume(@NotNull Consumer<E> consumer) {
        Objects.requireNonNull(consumer);

        if (!lockElse || !exists())
            return this;

        consumer.accept((E) element);
        return this;
    }

    @NotNull
    public Search<E> ifAbsent(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable);

        if (exists())
            return this;

        runnable.run();
        return this;
    }

    public boolean exists() {
        return element != null;
    }

    public boolean isAbsent() {
        return !exists();
    }

    @NotNull
    public Search<E> orElse(@Nullable E element) {
        if (this == NO_RESULT)
            return of(element);

        if (!exists()) {
            this.element = element;
            lockElse = true;
            return this;
        }

        lockElse = false;
        return this;
    }

    @NotNull
    public Search<E> supplyIfAbsent(@NotNull Supplier<E> supplier) {
        Objects.requireNonNull(supplier);

        if (this == NO_RESULT) {
            Search<E> search = of(supplier.get());
            search.lockElse = true;
            return search;
        }

        if (this.element == null) {
            this.element = supplier.get();
            lockElse = true;
            return this;
        }

        lockElse = false;
        return this;
    }

    public <F> Search<F> map(Function<? super E, ? extends F> mapper) {
        if (this == NO_RESULT)
            return NO_RESULT;

        Objects.requireNonNull(mapper);

        if (!exists())
            return NO_RESULT;

        element = mapper.apply((E) element);
        return (Search<F>) this;
    }

    public <F> F expression(Function<? super E, ? extends F> ifPresent, Supplier<F> orElse) {
        Objects.requireNonNull(orElse);

        if (this == NO_RESULT)
            return orElse.get();

        Objects.requireNonNull(ifPresent);

        if (exists())
            return ifPresent.apply((E) element);

        return orElse.get();
    }

    public <F> F expression(Function<? super E, ? extends F> ifPresent, F alternative) {
        Objects.requireNonNull(alternative);

        if (this == NO_RESULT)
            return alternative;

        Objects.requireNonNull(ifPresent);

        if (exists())
            return ifPresent.apply((E) element);

        return alternative;
    }

    public <O> Search<O> replace(O other) {
        if (this == NO_RESULT)
            return Search.of(other);

        element = other;
        return (Search<O>) this;
    }

    public Search<E> filter(Predicate<? super E> predicate) {
        if (this == NO_RESULT)
            return this;

        return predicate.test((E) element) ? this : NO_RESULT;
    }

    public E result() {
        return (E) element;
    }
    /*============================*/

}
