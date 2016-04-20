--
-- Crate Database: `fraud_detection`
--

CREATE DATABASE `fraud_detection`;

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE IF NOT EXISTS `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `t_number` varchar(45) DEFAULT NULL,
  `t_date` datetime DEFAULT NULL,
  `t_type` varchar(45) DEFAULT NULL,
  `t_amount` int(11) DEFAULT NULL,
  `country_code` varchar(100) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `isp` varchar(45) DEFAULT NULL,
  `beneficiary_acc_no` varchar(45) DEFAULT NULL,
  `customer_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `transaction_fraud`
--

CREATE TABLE IF NOT EXISTS `transaction_fraud` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `t_number` varchar(45) DEFAULT NULL,
  `t_date` datetime DEFAULT NULL,
  `t_type` varchar(45) DEFAULT NULL,
  `t_amount` int(11) DEFAULT NULL,
  `country_code` varchar(100) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `isp` varchar(45) DEFAULT NULL,
  `beneficiary_acc_no` varchar(45) DEFAULT NULL,
  `customer_id` varchar(100) DEFAULT NULL,
  `matched_use_cases` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

