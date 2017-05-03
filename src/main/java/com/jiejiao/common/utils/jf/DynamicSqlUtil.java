package com.jiejiao.common.utils.jf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动态sql 拼接
 * 
 * @author shizhiguo
 * @date 2017年1月22日 上午9:39:10
 */
public class DynamicSqlUtil {

	private static Logger log = LoggerFactory.getLogger(DynamicSqlUtil.class);

	private String order_con;// 默认排序字段

	public static final String Equal = "eq"; // 相等

	public static final String Not_Equal = "neq"; // 不相等

	public static final String Less_Than = "l"; // 小于

	public static final String Less_Equal = "le"; // 小于等于

	public static final String Greater_Equal = "ge"; // 大于等于

	public static final String Greater_Than = "g"; // 大于

	public static final String Fuzzy = "like"; // 模糊匹配 %xxx%

	public static final String Not_Empty = "nem"; // 不为空值的情况

	public static final String Empty = "em"; // 空值的情况

	public static final String In = "in"; // 在范围内

	public static final String Not_In = "nin"; // 不在范围内

	public static final String Sort = "sort";// 排序

	private ThreadLocal<String> whereSql = new ThreadLocal<String>();

	private ThreadLocal<String> orderSql = new ThreadLocal<String>();

	private ThreadLocal<ArrayList<Object>> paramList = new ThreadLocal<ArrayList<Object>>();

	/**
	 * map.put("name","123")<=>map.put("name,eq","123");
	 * 
	 * @param map
	 * @param removeKeys
	 *            要移除的key
	 */
	public DynamicSqlUtil(Map<String, Object> map) {
		this(map, null);
	}
	
	/**
	 * map.put("name","123")<=>map.put("name,eq","123");
	 * 
	 * @param map
	 * @param removeKeys
	 *            要移除的key
	 */
	public DynamicSqlUtil(Map<String, Object> map, Object removeKeys) {
		this(map, "id desc", removeKeys);
	}

	/**
	 * map.put("name","123")<=>map.put("name,eq","123");
	 * 
	 * @param map
	 * @param default_order_field
	 *            默认排序字段，默认id desc
	 * @param removeKeys
	 *            要移除的key
	 */
	public DynamicSqlUtil(Map<String, Object> map, String default_order_field, Object... removeKeys) {
		this.order_con = default_order_field;
		if (removeKeys.length > 0) {
			for (Object key : removeKeys) {
				if (map.containsKey(key)) {
					map.remove(key);
				}
			}
		}
		setWhereClause(map);
	}

	public void setWhereClause(Map<String, Object> map) {

		ArrayList<Object> params = new ArrayList<Object>();

		paramList.set(params);

		StringBuilder where = new StringBuilder();
		StringBuilder order = new StringBuilder();

		if (map != null) {

			Set<Entry<String, Object>> entrySet = map.entrySet();

			for (Entry<String, Object> entry : entrySet) {

				Object value = entry.getValue();
				String key = entry.getKey();

				if (key != null) {

					String[] keys = key.split(",");
					if (keys.length <= 0) {
						continue;
					}
					String fieldName = keys[0];
					String queryType = keys.length > 1 ? keys[1] : Equal;// 默认eq

					if (value != null && !"".equals(value)) {

						if (notEmpty(fieldName) && notEmpty(queryType)) {

							if (Sort.equals(queryType)) {// 升序
								order.append("," + fieldName + " " + value);
							} else if (Equal.equals(queryType)) {
								where.append("and " + fieldName + " = ? ");
								params.add(value);
							} else if (Not_Equal.equals(queryType)) {
								where.append("and " + fieldName + " <> ? ");
								params.add(value);
							} else if (Less_Than.equals(queryType)) {
								where.append("and " + fieldName + " < ? ");
								params.add(value);
							} else if (Less_Equal.equals(queryType)) {
								where.append("and " + fieldName + " <= ? ");
								params.add(value);
							} else if (Greater_Than.equals(queryType)) {
								where.append("and " + fieldName + " > ? ");
								params.add(value);
							} else if (Greater_Equal.equals(queryType)) {
								where.append("and " + fieldName + " >= ? ");
								params.add(value);
							} else if (Fuzzy.equals(queryType)) {
								where.append("and " + fieldName + " like ? ");
								params.add("%" + value + "%");
							} else if (Empty.equals(queryType)) {
								where.append("and " + fieldName + " is null ");
								params.add(value);
							} else if (Not_Empty.equals(queryType)) {
								where.append("and " + fieldName + " is not null ");
							} else if (In.equals(queryType)) {
								List list = (List) value;
								StringBuffer instr = new StringBuffer();
								where.append(" and " + fieldName + " in (");
								for (Object obj : list) {
									instr.append(notEmpty(instr.toString()) ? ",?" : "?");
									params.add(obj);
								}
								where.append(instr + ") ");
							}
						}

					}
				}

			}

		}

		String whereStr = where.toString();
		if (whereStr.startsWith("and")) {
			whereStr = whereStr.substring(3);
		}

		String orderStr = order.toString();
		if (orderStr.startsWith(",")) {
			orderStr = orderStr.substring(1);
		}

		whereSql.set(whereStr);
		orderSql.set(orderStr);
	}

	public List<Object> getParams() {
		return paramList.get();
	}

	public String getWhereSql() {
		String where = whereSql.get();
		if (!"".equals(where)) {
			where = " where " + where;
		}
		return where;
	}

	public String getOrderSql() {
		String order = orderSql.get();
		if ("".equals(order)) {
			order = order_con;
		}
		return " order by " + order;
	}

	private boolean notEmpty(String val) {
		return val != null && !"".equals(val);
	}

	public static void main(String[] args) {
		String key = "2323";
		String[] keys = key.split(",");
		System.out.println(keys.length);
		System.out.println(keys[0]);
	}

}