# Contributing

We will be using Github for development and version control.

## There will be three levels of the hierarchy.
- **L1**: The first level will be the main repository. This will be maintained by the **Architect**.
- **L2**: The second level will be the forks (one per each team lead) of the L1 repository. This will be maintained by **Team Leads**.
- **L3**: And last will be the forks (one per team member) of the L1 repository. This will be maintained by the **Team Member**.


![Untitled document (2)](https://user-images.githubusercontent.com/34399448/92390633-e6270180-f138-11ea-8567-8ab909baa12f.png)



## Here is the workflow from the bottom up:

1. Members or Leads will create an issue on `L1` for the feature they want to work on and create a new branch on `L3` for that, named as `{issue_number}-{short-solution-description}` and open a PR for it on `L2`, and in the description mention the issue number from `L1`.

2. If multiple members work on the same feature, there will be one representative member among them who will first create an issue and a new PR for it. Others will add the representative member's fork as remote and contribute to it.

3. Multiple team members can comment and review the PR and ask the responsible member to get it fixed. The Team Lead takes the call to merge it on `L2`.

4. PRs on `L1` will be entertained only from `L2` and will undergo review by the Architect and Manager. So Lead opens PR from `L2` to `L1` mentioning the issue number which gets fixed.

5. The architect takes the final call to merge the PR into the Master branch of `L1` and marks the issue as resolved.



## Coding Guidelines
1. We will follow Google's java style guide. https://google.github.io/styleguide/javaguide.html
2. Another blog worth going through https://medium.com/@rhamedy/a-short-summary-of-java-coding-best-practices-31283d0167d3

## Useful Links 
- [Using GIT](https://docs.github.com/en/github/using-git)
- [Commits](https://docs.github.com/en/github/committing-changes-to-your-project)
- [Issues and PR](https://docs.github.com/en/github/managing-your-work-on-github/managing-your-work-with-issues)
- [Labelling Issues](https://docs.github.com/en/github/managing-your-work-on-github/labeling-issues-and-pull-requests)
- [Linking a PR to an issue](https://docs.github.com/en/github/managing-your-work-on-github/linking-a-pull-request-to-an-issue)

```Documented By: Priyanshu```

```Thanks to Shruti, Ahmed and Rakesh for brainstorming on this.```
