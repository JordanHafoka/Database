package database.model;

public class QueryInfo
{
		/**
		 * The query being timed.
		 */
		private String query;
		/**
		 * The length of time (UNIX Standard) that it took to execute the query.
		 */
		private long queryTime;
		
		/**
		 * Constructor for a QueryInfo object. Must supply a String and long parameters so 
		 * all needed information is available at construction.
		 * @param query The query being executed.
		 * @param queryTime The time it took to execute the query.
		 */
		public QueryInfo(String query, long queryTime)
		{
			this.query = query;
			this.queryTime = queryTime;
		}

		/**
		 * Getter for the query.
		 * @return The query value of this QueryInfo.
		 */
		public String getQuery()
		{
			return query;
		}

		/**
		 * Getter for the execution time of the query.
		 * @return The time for this QueryInfo.
		 */
		public long getQueryTime()
		{
			return queryTime;
		}
}

