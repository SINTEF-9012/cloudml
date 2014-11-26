/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.facade.commands;

/**
 * Validate the loaded model if any, does nothing otherwise.
 *
 * The validation may or may not report warnings as well as errors, depending on
 * the given parameter.
 */
public class ValidateCommand extends CloudMlCommand {

    public static final boolean REPORT_ONLY_ERRORS = false;
    public static final boolean REPORT_WARNINGS_AND_ERRORS = true;

    private static final String VALIDATE_TEXT = "validate";
    private static final String WARNINGS_TEXT = "warnings";

    private final boolean raiseWarnings;

    /**
     * By default, we only check for errors and we discards warnings.
     */
    public ValidateCommand() {
        this(REPORT_ONLY_ERRORS);
    }

    /**
     * Customised validation
     *
     * @param reportWarnings indicated whether warnings shall be also reported
     */
    public ValidateCommand(boolean reportWarnings) {
        this.raiseWarnings = reportWarnings;
    }

    /**
     * @return true if the validation is expected to raise warnings as well as
     * errors, false otherwise;
     */
    public boolean mustReportWarnings() {
        return this.raiseWarnings;
    }

    @Override
    public void execute(CommandHandler target) {
        assert target != null: "Cannot trigger '" + toString() + "' on a null target!";

        target.handle(this);
    }

    @Override
    public String toString() {
        final String warnings = (mustReportWarnings() ? "" : " no warnings");
        return VALIDATE_TEXT + warnings;
    }

}
