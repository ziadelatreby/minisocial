# **Jakarta EE Development Environment Setup Guide**

This document outlines the steps required to set up a Jakarta EE development environment using Java 17, IntelliJ IDEA Ultimate, and WildFly. Please follow these instructions carefully to ensure a smooth development experience.

---

## **1. Install Java 17**

You can install Java 17 using a package manager or directly from Oracle:

* **Oracle JDK 17 Download**:
  [https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

Once installed, set the following environment variables (especially for macOS/Linux):

```bash
export JAVA_HOME=/path/to/java-17
export PATH=$JAVA_HOME/bin:$PATH
```

---

## **2. Install IntelliJ IDEA Ultimate**

Download and install IntelliJ IDEA Ultimate from the official JetBrains website:

* [https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)

---

## **3. Install WildFly (Recommended over JBoss 8)**

WildFly is preferred over JBoss 8 for Jakarta EE development. Download and extract WildFly 29:

* **WildFly 29 Download**:
  [https://github.com/wildfly/wildfly/releases/download/29.0.0.Final/wildfly-29.0.0.Final.zip](https://github.com/wildfly/wildfly/releases/download/29.0.0.Final/wildfly-29.0.0.Final.zip)

After extracting WildFly, configure the environment variable:

```bash
export WILDFLY_HOME=/path/to/wildfly-29.0.0.Final
```

You can also download JBoss 8 from here if needed for troubleshooting:

* [JBoss 8 Folder (Google Drive)](https://drive.google.com/drive/folders/1_NHV0aaa0dUR4y8TnNyeBFd11C48U-y6)

---

## **4. Create a New Jakarta EE Project in IntelliJ IDEA**

1. Open IntelliJ IDEA Ultimate.
2. Navigate to **File > New > Project**.
3. From the left panel, select **Jakarta EE**.
4. Fill in the project details:

   * **Name**: `minisocial-api`
   * **Location**: Choose any directory you prefer
   * **Do NOT initialize with a Git repository** (you will pull an existing one later)
5. Under **Project Template**, select:

   * **REST service**
6. Set the **Application Server** to your WildFly installation (`$WILDFLY_HOME`).
7. Configure the build settings:

   * **Language**: Java
   * **Build system**: Maven
   * **GroupId**: `com.minisocial`
   * **ArtifactId**: `minisocial-api`
   * **Java version**: **17 (Important!)**
8. Click **Next**.

---

## **5. Configure Jakarta EE Version**

* In the **Jakarta EE Version** dropdown (top-left of the wizard), choose:

  * Recommended: **Jakarta EE 9.1**
  * Acceptable alternatives: **Jakarta EE 10** or **11**
  * **Do NOT select Java EE 8** or older Jakarta versions.

* Select **Full Platform**.

* Click **Create** to generate the project.

---

## **6. Pull Existing Project Code**

After project creation, pull the latest code from the existing Git repository:

```bash
git fetch origin
git reset --hard origin
```

Verify that the project builds and runs successfully before proceeding with development.

---

## **7. Optional: Set Up VS Code for Jakarta EE (Alternative to IntelliJ IDEA)**

If you prefer to use **Visual Studio Code**, install the following extensions:

* **Extension Pack for Java**
* **MicroProfile Extension Pack**
* **XML Tools**
* **Maven for Java**
* **Debugger for Java**

---

## **Final Notes**

* WildFly supports modern Jakarta EE standards and is actively maintained. It is the recommended application server for this project.
* Make sure to consistently use **Java 17** across your environment to avoid compatibility issues.
* IntelliJ IDEA Ultimate is required due to advanced Jakarta EE support not available in the Community edition.
