package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * This class implements a {@link IFilter}. It checks a {@link StudentRecord} against
 * a list of {@link ConditionalExpression}s.
 * @author ltomic
 *
 */

public class QueryFilter implements IFilter {
	
	/**
	 * List of conditional expressions with which a {@link StudentRecord} will be tested.
	 */
	List<ConditionalExpression> expressionList;
	
	/**
	 * Constructs a {@link QueryFilter} with given list as a list of conditional expressions.
	 * @param expressionList
	 */
	public QueryFilter(List<ConditionalExpression> expressionList) {
		super();
		this.expressionList = Objects.requireNonNull(expressionList);
	}

	/**
	 * Tests a given {@link StudentRecord} against a list of conditional expressions.
	 * @param record student to be tested
	 * @return true if {@link StudentRecord} passes satisfies all conditional expressions.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : expressionList) {
			if (!expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record), expr.getStringLiteral()))
				return false;
		}

		return true;
	}

}
