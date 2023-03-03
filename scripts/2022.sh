#!/bin/sh

function accept_use_case() {
# ./scripts/execute_360x_testing.sh $1 accept initiator	\
#	$RULES $PRODUCTION/in/TI-$1/Accept-Use-Case $PRODUCTION/out/TI-$1-output-Wed/Accept/initiator

 ./scripts/execute_360x_testing.sh $1 accept recipient	\
	$RULES $PRODUCTION/in/TI-$1/Accept-Use-Case $PRODUCTION/out/TI-$1-output-Wed/Accept/recipient
}

PRODUCTION=/opt/connectathons/2022-combined/Production_Testing/360X
RULES=./rules


touch /tmp/batch
rm -r /tmp/batch


# 72922
#accept_use_case 72922

# 73903
#accept_use_case 73903

# 74606
accept_use_case 74606

# ./scripts/execute_360x_testing.sh 73903 accept initiator $RULES $PRODUCTION/in/TI-73903/Accept-Use-Case $PRODUCTION/out/TI-73903-output/Accept/initiator

exit 1

 ./scripts/execute_360x_testing.sh 71642 accept recipient $RULES $PRODUCTION/TI-71642/Accept-Use-Case $PRODUCTION/TI-71642-output/Accept/recipient

 ./scripts/execute_360x_testing.sh 71642 decline initiator $RULES $PRODUCTION/TI-71642/Declined-Use-Case $PRODUCTION/TI-71642-output/Declined/initiator
 ./scripts/execute_360x_testing.sh 71642 decline recipient $RULES $PRODUCTION/TI-71642/Declined-Use-Case $PRODUCTION/TI-71642-output/Declined/recipient

# 71672
 ./scripts/execute_360x_testing.sh 71672 accept initiator $RULES $PRODUCTION/TI-71672/Accept-Use-Case $PRODUCTION/TI-71672-output/Accept/initiator
 ./scripts/execute_360x_testing.sh 71672 accept recipient $RULES $PRODUCTION/TI-71672/Accept-Use-Case $PRODUCTION/TI-71672-output/Accept/recipient

 ./scripts/execute_360x_testing.sh 71672 decline initiator $RULES $PRODUCTION/TI-71672/Declined-Use-Case $PRODUCTION/TI-71672-output/Declined/initiator
 ./scripts/execute_360x_testing.sh 71672 decline recipient $RULES $PRODUCTION/TI-71672/Declined-Use-Case $PRODUCTION/TI-71672-output/Declined/recipient

# 71887
 ./scripts/execute_360x_testing.sh 71887 accept initiator $RULES $PRODUCTION/TI-71887/Accept-Use-Case $PRODUCTION/TI-71887-output/Accept/initiator
 ./scripts/execute_360x_testing.sh 71887 accept recipient $RULES $PRODUCTION/TI-71887/Accept-Use-Case $PRODUCTION/TI-71887-output/Accept/recipient

# 71895
 ./scripts/execute_360x_testing.sh 71895 decline initiator $RULES $PRODUCTION/TI-71895/Declined-Use-Case $PRODUCTION/TI-71895-output/Declined/initiator
 ./scripts/execute_360x_testing.sh 71895 decline recipient $RULES $PRODUCTION/TI-71895/Declined-Use-Case $PRODUCTION/TI-71895-output/Declined/recipient

ls -l /tmp/batch
