package com.worldline.clic.utils.mvn;

import static com.worldline.clic.utils.Messages.*;
import static joptsimple.util.RegexMatcher.regex;

import java.util.Properties;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.util.KeyValuePair;

public class CommandLine {

	/**
	 * This {@link OptionSpec} contains all the Maven parameters provided using
	 * the syntax <code>-Dparam=value</code>
	 */
	private OptionSpec<KeyValuePair> mavenParameters;

	/**
	 * This {@link OptionSpec} contains the Maven reference which is provided by
	 * the user using the format <code>groupId:artifactId:version</code>
	 */
	private OptionSpec<String> mavenReference;

	/**
	 * This {@link OptionSpec} contains the Maven command which should be
	 * executed on top of the parameters provided
	 */
	private OptionSpec<String> mavenCommand;

	public void configureParser(final OptionParser parser) {
		mavenReference = parser
				.accepts(MAVEN_REFERENCE.value(),
						MAVEN_REFERENCE_DESCRIPTION.value())
				.withRequiredArg()
				.describedAs(MAVEN_REFERENCE_ARG.value())
				.ofType(String.class)
				.withValuesConvertedBy(
						regex("([a-zA-Z_0-9-_.])+[:]([a-zA-Z_0-9-_.])+[:]([a-zA-Z_0-9-_.])+"))
				.required();
		parser.accepts(GENERATE_POM.value(), GENERATE_POM_DESCRIPTION.value());
		mavenCommand = parser
				.accepts(MAVEN_CMD.value(), MAVEN_CMD_DESCRIPTION.value())
				.withRequiredArg()
				.describedAs(MAVEN_CMD_ARG.value())
				.ofType(String.class)
				.withValuesConvertedBy(
						regex("([a-zA-Z_0-9-_.])+([:]([a-zA-Z_0-9-_.])+)?"))
				.ofType(String.class).required();
		mavenParameters = parser
				.accepts(JVM_PARAM.value(), JVM_PARAM_DESCRIPTION.value())
				.withRequiredArg().describedAs(JVM_PARAM_ARG.value())
				.ofType(KeyValuePair.class);
	}

	public static Properties computeMavenParameters(final OptionSet options,
			final OptionSpec<KeyValuePair> params) {
		if (!options.has(params))
			return new Properties();
		final Properties props = new Properties();
		for (final KeyValuePair pair : options.valuesOf(params))
			props.put(pair.key, pair.value);
		return props;
	}

	/**
	 * @return the mavenParameters
	 */
	public OptionSpec<KeyValuePair> getMavenParameters() {
		return mavenParameters;
	}

	/**
	 * @return the mavenReference
	 */
	public OptionSpec<String> getMavenReference() {
		return mavenReference;
	}

	/**
	 * @return the mavenCommand
	 */
	public OptionSpec<String> getMavenCommand() {
		return mavenCommand;
	}

}
