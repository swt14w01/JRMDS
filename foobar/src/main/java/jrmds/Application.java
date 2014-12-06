package jrmds;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

@Configuration
@EnableAutoConfiguration
@EnableNeo4jRepositories(basePackages = "jrmds")
@ComponentScan
public class Application extends Neo4jConfiguration {
	public static GraphDatabaseService repo;

	public Application() {
		repo = new SpringRestGraphDatabase("http://[::1]:7474/db/data");
		setBasePackage("jrmds");
	}

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		return repo;
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}