#!/bin/sh
TESTING=/opt/CLI-testing/2021
PRODUCTION=/opt/CLI-testing/2021-production/2021.03.05
RULES=/opt/CLI-testing
RULES=./rules

touch /tmp/batch
rm -r /tmp/batch
mkdir /tmp/batch

# 71631
#./scripts/execute_360x_testing.sh 71631 accept recipient $RULES $PRODUCTION/TI-71631/Accept-Use-Case $PRODUCTION/TI-71631-output/Accept/recipient
 ./scripts/execute_360x_testing.sh 71631 decline recipient $RULES $PRODUCTION/TI-71631/Declined-Use-Case $PRODUCTION/TI-71631-output/Declined/recipient
exit

# 71670
 ./scripts/execute_360x_testing.sh 71670 accept initiator $RULES $PRODUCTION/TI-71670/Accept-Use-Case $PRODUCTION/TI-71670-output/Accept/initiator

# 71671
 ./scripts/execute_360x_testing.sh 71671 accept initiator $RULES $PRODUCTION/TI-71671/Accept-Use-Case $PRODUCTION/TI-71671-output/Accept/initiator
 ./scripts/execute_360x_testing.sh 71671 accept recipient $RULES $PRODUCTION/TI-71671/Accept-Use-Case $PRODUCTION/TI-71671-output/Accept/recipient

# 71632
 ./scripts/execute_360x_testing.sh 71632 accept initiator $RULES $PRODUCTION/TI-71632/Accept-Use-Case $PRODUCTION/TI-71632-output/Accept/initiator
 ./scripts/execute_360x_testing.sh 71632 accept recipient $RULES $PRODUCTION/TI-71632/Accept-Use-Case $PRODUCTION/TI-71632-output/Accept/recipient

# 71632
 ./scripts/execute_360x_testing.sh 71632 decline initiator $RULES $PRODUCTION/TI-71632/Declined-Use-Case $PRODUCTION/TI-71632-output/Accept/initiator
 ./scripts/execute_360x_testing.sh 71632 decline recipient $RULES $PRODUCTION/TI-71632/Declined-Use-Case $PRODUCTION/TI-71632-output/Accept/recipient

# NG driven testing
# ./scripts/execute_360x_testing.sh ng_request accept initiator $RULES \
#	$PRODUCTION/NG-2021.03.03/ng_initiator $PRODUCTION/NG-2021.03.03/ng_initiator-out
#
# ./scripts/execute_360x_testing.sh ng_accept  accept recipient $RULES \
#	$PRODUCTION/NG-2021.03.03/qvera_ng_accept2 $PRODUCTION/NG-2021.03.03/qvera_ng_accept2-out
#
# ./scripts/execute_360x_testing.sh ng_decline decline recipient $RULES \
#	$PRODUCTION/NG-2021.03.03/qvera_ng_decline2 $PRODUCTION/NG-2021.03.03/qvera_ng_decline2-out
#
# ./scripts/execute_360x_testing.sh qvera_request accept initiator $RULES \
#	$PRODUCTION/NG-2021.03.03/qvera_ng_accept2 $PRODUCTION/NG-2021.03.03/qvera_initiator-out
#
## 71642
# ./scripts/execute_360x_testing.sh 71642 accept initiator $RULES $PRODUCTION/TI-71642/Accept-Use-Case $PRODUCTION/TI-71642-output/Accept/initiator
# ./scripts/execute_360x_testing.sh 71642 accept recipient $RULES $PRODUCTION/TI-71642/Accept-Use-Case $PRODUCTION/TI-71642-output/Accept/recipient
#
# ./scripts/execute_360x_testing.sh 71642 decline initiator $RULES $PRODUCTION/TI-71642/Declined-Use-Case $PRODUCTION/TI-71642-output/Declined/initiator
# ./scripts/execute_360x_testing.sh 71642 decline recipient $RULES $PRODUCTION/TI-71642/Declined-Use-Case $PRODUCTION/TI-71642-output/Declined/recipient
#
## 71672
##./scripts/execute_360x_testing.sh 71672 accept initiator $RULES $PRODUCTION/TI-71672/Accept-Use-Case $PRODUCTION/TI-71672-output/Accept/initiator
##./scripts/execute_360x_testing.sh 71672 accept recipient $RULES $PRODUCTION/TI-71672/Accept-Use-Case $PRODUCTION/TI-71672-output/Accept/recipient
#
##./scripts/execute_360x_testing.sh 71672 decline initiator $RULES $PRODUCTION/TI-71672/Declined-Use-Case $PRODUCTION/TI-71672-output/Declined/initiator
##./scripts/execute_360x_testing.sh 71672 decline recipient $RULES $PRODUCTION/TI-71672/Declined-Use-Case $PRODUCTION/TI-71672-output/Declined/recipient
#
## 71887
##./scripts/execute_360x_testing.sh 71887 accept initiator $RULES $PRODUCTION/TI-71887/Accept-Use-Case $PRODUCTION/TI-71887-output/Accept/initiator
##./scripts/execute_360x_testing.sh 71887 accept recipient $RULES $PRODUCTION/TI-71887/Accept-Use-Case $PRODUCTION/TI-71887-output/Accept/recipient
#
## 71895
##./scripts/execute_360x_testing.sh 71895 decline initiator $RULES $PRODUCTION/TI-71895/Declined-Use-Case $PRODUCTION/TI-71895-output/Declined/initiator
##./scripts/execute_360x_testing.sh 71895 decline recipient $RULES $PRODUCTION/TI-71895/Declined-Use-Case $PRODUCTION/TI-71895-output/Declined/recipient
#
#ls -l /tmp/batch
