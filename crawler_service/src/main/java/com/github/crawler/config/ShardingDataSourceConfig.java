package com.github.crawler.config;

import com.dangdang.ddframe.rdb.sharding.config.common.api.ShardingRuleBuilder;
import com.dangdang.ddframe.rdb.sharding.config.yaml.internel.YamlConfig;
import com.dangdang.ddframe.rdb.sharding.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;

@Configuration
public class ShardingDataSourceConfig {

	@Bean
	public DataSource getDataSource(@Value(value = "${custom.environment}") String env)
			throws SQLException {
		InputStream inputStream = this.getClass().getResourceAsStream("/config/" + env + "/sharding-jdbc.yaml");
		YamlConfig yc = new Yaml(new Constructor(YamlConfig.class)).loadAs(inputStream, YamlConfig.class);
		return  new ShardingDataSource(new ShardingRuleBuilder("sharding-jdbc",yc).build(), yc.getProps());
	}

}