# Terraform in local

To run terraform use:

- PROD:
    - terraform workspace select prod
    - terraform plan -var-file="prod.tfvars"
    - terraform apply -var-file="prod.tfvars"
- QA:
    - terraform workspace select qa
    - terraform plan -var-file="qa.tfvars"
    - terraform apply -var-file="qa.tfvars"

# CI/CD GH Action

Make sure the version of the Alias of lambda in both environments while promoting to develop/main
Two workspaces created in [Terraform Cloud](https://app.terraform.io) using CI/CD pipelines
of [Hashicorp](https://github.com/hashicorp/tfc-workflows-github).

# SonarCloud
Analytics via [SonarCloud](https://sonarcloud.io/project/configuration/GitHubActions?id=lblasnar_awslamda)
