To run terraform use:

- PROD:
    - terraform plan -var-file="prod.tfvars"
    - terraform apply -var-file="prod.tfvars"
- QA:
    - terraform plan -var-file="qa.tfvars"
    - terraform apply -var-file="qa.tfvars"