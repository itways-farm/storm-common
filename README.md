To publish storm-common to JitPack:


git tag common-v1.0.0
git push origin common-v1.0.0



The workflow creates a GitHub Release → JitPack auto-builds it. External consumers then use:

<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
<dependency>
  <groupId>com.github.YOUR_GITHUB_USERNAME</groupId>
  <artifactId>storm-common</artifactId>
  <version>common-v1.0.0</version>
</dependency>
