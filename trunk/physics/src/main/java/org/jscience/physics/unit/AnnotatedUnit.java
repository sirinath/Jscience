/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import java.util.Map;
import org.jscience.physics.quantity.Quantity;
import org.jscience.physics.unit.PhysicsUnit;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> This class represents an annotated unit. It  allows for unit specialization
 *     and annotation without changing the unit semantic. For example:[code]
 *        public class Size implements Length {
 *             private double meters;
 *             ...
 *             public static class PhysicsUnit extends AnnotatedUnit<Length> {
 *                  private PhysicsUnit(org.jscience.physics.PhysicsUnit<Length> realUnit, String annotation) {
 *                      super(actualUnit, annotation);
 *                  }
 *                  public static Size.PhysicsUnit METER = new Size.PhysicsUnit(SI.METRE, "size"); // Equivalent to SI.METRE
 *                  public static Size.PhysicsUnit INCH = new Size.PhysicsUnit(NonSI.INCH, "size"); // Equivalent to NonSI.INCH
 *             }
 *        }[/code]</p>
 * <p> Annotation are often written between curly braces behind units
 *     but they do not change, for example "%{vol}", "kg{total}", or
 *     "{RBC}" (for "red blood cells") are equivalent to "%", "kg", and "1"
 *      respectively.</p>
 *
 * <p><b>Note:</b> This class supports the {@code UnitFormat} implementation {@code LocalFormat}, 
 * hence it is currently part of the internal format API. If required and made public, it's possible
 * to locate in the "unit" package.</p>    
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class AnnotatedUnit<Q extends Quantity<Q>> extends PhysicsUnit<Q> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Holds the annotation.
     */
    private final String annotation;

    /**
     * Holds the actual unit, never an annotated unit.
     */
    private final PhysicsUnit<Q> actualUnit;

    public PhysicsUnit<Q> getActualUnit() {
		return actualUnit;
	}

	/**
     * Creates an annotated unit for the specified unit.
     *
     * @param actualUnit the real unit.
     * @param annotation the annotation.
     */
    public AnnotatedUnit(PhysicsUnit<Q> actualUnit, String annotation) {
        this.actualUnit = (actualUnit instanceof AnnotatedUnit<?>)
                ? ((AnnotatedUnit<Q>) actualUnit).actualUnit : actualUnit;
        this.annotation = annotation;
    }

    /**
     * Returns an annotated unit equivalent to this unit. The annotation
     * does not change the unit semantic.
     * Annotation are often written between curly braces behind units.
     * For example, annotated units "%{vol}", "kg{total}", or "{RBC}"
     * (for "red blood cells") are equivalent to "%", "kg", and "1" respectively.
     *
     * @param annotation the new symbol for the alternate unit.
     * @return the alternate unit.
     * @throws UnsupportedOperationException if this unit is not a metric unit.
     * @throws IllegalArgumentException if the specified symbol is already
     *         associated to a different unit.
     */
    public final PhysicsUnit<Q> annotate(String annotation) {
        return new AnnotatedUnit<Q>(this, annotation);
    }
    
    /**
     * Returns the annotation (if any) of this unit.
     * The default implementation returns <code>null</code> (no annotation).
     *
     * @return this unit annotation or <code>null</code> if this unit has not
     *         specific symbol associated with (e.g. product of units).
     */
    public String getAnnotation() {
        return annotation;
    }

   @Override
    public String getSymbol() {
        return actualUnit.getSymbol();
    }

    @Override
    public Map<PhysicsUnit<?>, Integer> getProductUnits() {
        return actualUnit.getProductUnits();
    }
  
   @Override
    public PhysicsUnit<Q> toMetric() {
        return actualUnit.toMetric();
    }

    @Override
    public UnitConverter getConverterToMetric() {
        return actualUnit.getConverterToMetric();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof AnnotatedUnit<?>))
            return false;
        AnnotatedUnit<?> thatUnit = (AnnotatedUnit<?>) that;
        return this.actualUnit.equals(thatUnit.actualUnit) &&
                this.annotation.equals(thatUnit.annotation);
    }

    @Override
    public int hashCode() {
        return actualUnit.hashCode() + annotation.hashCode();
    }
}
