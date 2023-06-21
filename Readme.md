To run terraform use:

- PROD:
  - terraform workspace select prod
  - terraform plan -var-file="prod.tfvars"
  - terraform apply -var-file="prod.tfvars"
- QA:
  - terraform workspace select qa
  - terraform plan -var-file="qa.tfvars"
  - terraform apply -var-file="qa.tfvars"