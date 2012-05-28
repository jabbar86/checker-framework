package checkers.flow.analysis.checkers;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;

import checkers.basetype.BaseTypeChecker;
import checkers.types.AbstractBasicAnnotatedTypeFactory;
import checkers.util.AnnotationUtils;

/**
 * The default dataflow analysis used in the Checker Framework.
 * 
 * @author Stefan Heule
 * 
 */
public class CFAnalysis extends
        CFAbstractAnalysis<CFValue, CFStore, CFTransfer> {

    public CFAnalysis(
            AbstractBasicAnnotatedTypeFactory<? extends BaseTypeChecker, CFValue, CFStore, CFTransfer, CFAnalysis> factory,
            ProcessingEnvironment env, BaseTypeChecker checker) {
        super(factory, env, checker);
    }

    @Override
    protected CFTransfer createTransferFunction() {
        return new CFTransfer(this);
    }

    @Override
    protected CFStore createEmptyStore() {
        return new CFStore(this);
    }

    @Override
    protected CFStore createCopiedStore(CFStore s) {
        return new CFStore(this, s);
    }

    @Override
    protected/* @Nullable */CFValue createAbstractValue(
            Set<AnnotationMirror> annotations) {
        return defaultCreateAbstractValue(annotations, legalAnnotations, this);
    }

    /**
     * Only uses the legal annotations in {@code annotations} and
     * {@code effectiveAnnotations}, and creates a {@link CFValue}.
     */
    public static CFValue defaultCreateAbstractValue(
            Set<AnnotationMirror> annotations,
            Set<AnnotationMirror> legalAnnotations,
            CFAbstractAnalysis<CFValue, ?, ?> analysis) {
        Set<AnnotationMirror> as = new HashSet<>();
        for (AnnotationMirror a : annotations) {
            if (AnnotationUtils.containsSameIgnoringValues(legalAnnotations, a)) {
                as.add(a);
            }
        }
        if (as.size() == 0) {
            return null;
        }
        return new CFValue(analysis, as);
    }

}
