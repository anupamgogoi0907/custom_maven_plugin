package com.anupam;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Mojo(name = "hello")
public class MyMojo extends AbstractMojo {

    @Component
    MavenProject mavenProject;

    @Parameter(defaultValue = "${localRepository}", readonly = true)
    protected ArtifactRepository localRepository;

    @Component
    ArtifactFactory artifactFactory;

    @Component
    ArtifactResolver artifactResolver;

    @Parameter(property = "msg")
    private String msg;


    public void execute()
            throws MojoExecutionException {
        getLog().info("Hello " + msg);
        try {
            Artifact artifact = resolveMavenProjectArtifact();

        } catch (MojoFailureException e) {
            e.printStackTrace();
        }
    }

    protected Artifact resolveMavenProjectArtifact() throws MojoFailureException {
        Artifact artifact = artifactFactory.createArtifact(mavenProject.getGroupId(), mavenProject.getArtifactId(),
                mavenProject.getVersion(), "", "jar");
        try {
            artifactResolver.resolve(artifact, new ArrayList<ArtifactRepository>(), localRepository);
        } catch (ArtifactResolutionException e) {
            throw new MojoFailureException("Couldn't resolve artifact [" + artifact + "]");
        } catch (ArtifactNotFoundException e) {
            throw new MojoFailureException("Couldn't resolve artifact [" + artifact + "]");
        }

        return artifact;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
