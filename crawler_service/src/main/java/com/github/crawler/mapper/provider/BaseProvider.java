package com.github.crawler.mapper.provider;


import com.github.crawler.entity.BaseEntity;
import com.github.crawler.mapper.provider.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseProvider {

	public String countSql(@Param("wheres") Wheres ws, @Param("clazz") Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		List<Where> wheres = ws.getWheres();
		return new SQL() {
			{
				SELECT("count(*)").FROM(tableName);
				if (wheres != null && !wheres.isEmpty()) {
					for (int i = 0; i < wheres.size(); i++) {
						Where w = wheres.get(i);
						WHERE("`" + w.getField() + "` " + w.getOp() + " #{wheres.wheres[" + i + "].value} ");
					}
				}
			}
		}.toString();
	}

	public String insertEntity(BaseEntity entity) throws Exception {
		Class<?> clazz = entity.getClass();
		final String tableName = this.getTableName(clazz);
		List<String> columns_ = new ArrayList<String>();
		List<String> values_ = new ArrayList<String>();

		getColumnsMapping(columns_,values_,clazz);

//		for (Field f : clazz.getDeclaredFields()) {
//			String column = f.getName();
//			columns_.add("`" + column + "`");
//			values_.add("#{" + column + "}");
//		}
//		Class<?> sucla = clazz.getSuperclass();
//		for (Field f : sucla.getDeclaredFields()) {
//			String column = f.getName();
//			columns_.add("`" + column + "`");
//			values_.add("#{" + column + "}");
//		}
		String columns = StringUtils.join(columns_, ",");
		String values = StringUtils.join(values_, ",");
		return new SQL().INSERT_INTO(tableName).VALUES(columns, values).toString();
	}

	private void getColumnsMapping(List<String> columns_ ,List<String> values_ ,Class<?> clazz ){
		for (Field f : clazz.getDeclaredFields()) {
			String column = f.getName();
			columns_.add("`" + column + "`");
			values_.add("#{" + column + "}");
		}
		if(clazz == BaseEntity.class){
			return;
		}else {
			getColumnsMapping( columns_, values_,clazz.getSuperclass()   );
		}
	}




	public String deleteSqlById(@Param("id") long id, @Param("clazz") Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		return new SQL().DELETE_FROM(tableName).WHERE("id=#{id}").toString();
	}

//	public String deleteSqlByIdUid(Map<String, Object> param) {
//		String tableName = this.getTableName((Class<?>) param.get("clazz"));
//		return new SQL().DELETE_FROM(tableName).WHERE("id=#{id}").WHERE("uid=#{uid}").toString();
//	}

	public String deleteSql(@Param("wheres") Wheres ws, @Param("clazz") Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		List<Where> wheres = ws.getWheres();
		String sql = new SQL() {
			{
				DELETE_FROM(tableName);
				if (wheres != null && !wheres.isEmpty()) {
					for (int i = 0; i < wheres.size(); i++) {
						Where w = wheres.get(i);
						WHERE("`" + w.getField() + "`" + w.getOp() + " #{wheres.wheres[" + i + "].value} ");
					}
				}
			}
		}.toString();
		return sql;
	}

	public String updateEntity(BaseEntity entity) throws Exception {
		Class<?> clazz = entity.getClass();
		final String tableName = this.getTableName(clazz);
		Field[] fields = clazz.getDeclaredFields();
		List<String> sets_ = new ArrayList<String>();
		for (Field f : fields) {
			String column = f.getName();
			if ("id".equals(column) || "create_at".equals(column))
				continue;
			sets_.add("`" + column + "` = " + "#{" + column + "}");
		}
		Class<?> sucla = clazz.getSuperclass();
		Field[] sufields = sucla.getDeclaredFields();
		for (Field f : sufields) {
			String column = f.getName();
			if ("id".equals(column) || "create_at".equals(column))
				continue;
			sets_.add("`" + column + "` = " + "#{" + column + "}");
		}
		String sets = StringUtils.join(sets_, ",");
		String where = "id = #{id} ";
		return new SQL().UPDATE(tableName).SET(sets).WHERE(where).toString();
	}

//	public String updateSqlByIdUid(BaseEntity entity) {
//		Class<?> clazz = entity.getClass();
//		final String tableName = this.getTableName(clazz);
//		Field[] fields = clazz.getDeclaredFields();
//		List<String> sets_ = new ArrayList<String>();
//		for (Field f : fields) {
//			String column = f.getName();
//			if ("id".equals(column) || "create_at".equals(column))
//				continue;
//			sets_.add("`" + column + "` = " + "#{" + column + "}");
//		}
//		Class<?> sucla = clazz.getSuperclass();
//		Field[] sufields = sucla.getDeclaredFields();
//		for (Field f : sufields) {
//			String column = f.getName();
//			if ("id".equals(column) || "create_at".equals(column))
//				continue;
//			sets_.add("`" + column + "` = " + "#{" + column + "}");
//		}
//		String sets = StringUtils.join(sets_, ",");
//		String where = "id = #{id} and uid=#{uid} ";
//		return new SQL().UPDATE(tableName).SET(sets).WHERE(where).toString();
//	}

	public String updateSql(@Param("sets") Sets sets, @Param("wheres") Wheres ws, @Param("clazz") Class<?> clazz)
			throws Exception {
		final String tableName = this.getTableName(clazz);
		List<Set> sets_ = sets.getSets();
		List<Where> wheres = ws.getWheres();
		String sql = new SQL() {
			{
				UPDATE(tableName);
				if (sets != null && !sets_.isEmpty()) {
					for (int i = 0; i < sets_.size(); i++) {
						Set s = sets_.get(i);
						SET("`" + s.getField() + "` = #{sets.sets[" + i + "].value} ");
					}
				}
				if (wheres != null && !wheres.isEmpty()) {
					for (int i = 0; i < wheres.size(); i++) {
						Where w = wheres.get(i);
						WHERE("`" + w.getField() + "` " + w.getOp() + " #{wheres.wheres[" + i + "].value} ");
					}
				}
			}
		}.toString();
		return sql;
	}

	public String updateSqlById(@Param("sets") Sets sets, @Param("id") long id, @Param("clazz") Class<?> clazz)
			throws Exception {
		final String tableName = this.getTableName(clazz);
		List<Set> sets_ = sets.getSets();
		String sql = new SQL() {
			{
				UPDATE(tableName);
				if (sets != null && !sets_.isEmpty()) {
					for (int i = 0; i < sets_.size(); i++) {
						Set s = sets_.get(i);
						SET("`" + s.getField() + "` = #{sets.sets[" + i + "].value} ");
					}
				}
				if (id > 0) {
					WHERE("`id` = #{id} ");
				}
			}
		}.toString();
		return sql;
	}

	public String selectSqlWOL(@Param("whereOrderLimit") WhereOrderLimit whereOrderLimit,
			@Param("clazz") Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		List<Where> wheres = whereOrderLimit.getWheres();
		Order order = whereOrderLimit.getOrder();
		Limit limit = whereOrderLimit.getLimit();
		String force_index=whereOrderLimit.getForce_index();
		String sql = new SQL() {
			{
				SELECT("*");
				FROM(tableName);
				if (wheres != null && !wheres.isEmpty()) {
					for (int i = 0; i < wheres.size(); i++) {
						Where w = wheres.get(i);
						if (w.getOp().equals(Where.OperatorE.isNull.getSymbol())) {
							WHERE("`" + w.getField() + "` is null");
						} else {
							WHERE("`" + w.getField() + "` " + w.getOp() + " #{whereOrderLimit.wheres[" + i
									+ "].value} ");
						}

					}

				}
				if (order != null) {
					ORDER_BY("`" + order.getColumn() + "` " + order.getSort());
				}
			}
		}.toString();
		if (limit != null) {
			sql = sql + " limit " + limit.getOffset() + "," + limit.getRows();
		}
		if(StringUtils.isBlank(force_index)) {
			return sql;
		}
		return sql.replace(tableName, tableName+" force index(`"+force_index+"`) ");
	}

	public String selectSql(@Param("wheres") Wheres ws, @Param("order") Order order, @Param("limit") Limit limit,
			@Param("clazz") Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		List<Where> wheres = ws.getWheres();
		String sql = new SQL() {
			{
				SELECT("*");
				FROM(tableName);
				if (wheres != null && !wheres.isEmpty()) {
					for (int i = 0; i < wheres.size(); i++) {
						Where w = wheres.get(i);
						if (w.getOp().equals(Where.OperatorE.isNull.getSymbol())) {
							WHERE("`" + w.getField() + "` is null");
						} else {
							WHERE("`" + w.getField() + "` " + w.getOp() + " #{wheres.wheres[" + i + "].value} ");
						}

					}

				}
				if (order != null) {
					ORDER_BY("`" + order.getColumn() + "` " + order.getSort());
				}
			}
		}.toString();
		if (limit != null) {
			sql = sql + " limit " + limit.getOffset() + "," + limit.getRows();
		}
		return sql;
	}

	public String selectSqlId(@Param("id") long id, @Param("clazz") Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		String sql = new SQL() {
			{
				SELECT("*");
				FROM(tableName);
				WHERE("id = #{id}");
			}
		}.toString();
		return sql;
	}

	public String selectSqlAll(Class<?> clazz) throws Exception {
		final String tableName = this.getTableName(clazz);
		String sql = new SQL() {
			{
				SELECT("*");
				FROM(tableName);
			}
		}.toString();
		return sql;
	}

	private String getTableName(Class<?> clazz) throws Exception {
		if (clazz.getAnnotation(TableName.class) == null) {
			throw new RuntimeSqlException("@TableName is not find "+clazz);
		}
		return clazz.getAnnotation(TableName.class).value();
	}
}
