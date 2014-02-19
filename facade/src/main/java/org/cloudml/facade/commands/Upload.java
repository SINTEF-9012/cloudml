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
 * Request for the direct upload of a local resources on one of the artifact
 * managed through the deployment model
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class Upload extends ManageableCommand {

	private final String artifactId;
	private final String localPath;
	private final String remotePath;

	/**
	 * Create a request for a direct upload of a local resources on one of the
	 * existing artifact.
	 * 
	 * @param artifactI
	 *            the ID of the artifact where the resources shall be uploaded
	 * @param localPath
	 *            the path to the local resources to upload
	 * @param remotePath
	 *            the path on the remote host, where the resources shall be
	 *            stored
	 */
	public Upload(CommandHandler handler, final String artifactId, final String localPath,
			final String remotePath) {
		super(handler);
		this.artifactId = artifactId;
		this.localPath = localPath;
		this.remotePath = remotePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cloudml.facade.commands.CloudMlCommand#execute(org.cloudml.facade
	 * .commands.CommandHandler)
	 */
	public void execute(CommandHandler handler) {
		handler.handle(this);
	}

	/**
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * @return the localPath
	 */
	public String getLocalPath() {
		return localPath;
	}

	/**
	 * @return the remotePath
	 */
	public String getRemotePath() {
		return remotePath;
	}

}
